package lanej.inventorysystem.controller;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import lanej.inventorysystem.InventoryApplication;
import lanej.inventorysystem.model.InHouse;
import lanej.inventorysystem.model.Inventory;
import lanej.inventorysystem.model.Outsourced;
import lanej.inventorysystem.model.Part;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * The ModifyPart class is responsible for managing the user interface and functionality of the Modify Part
 * screen in the inventory management system. This class implements the Initializable interface from the JavaFX API.
 * It contains radio buttons (for selecting the Part type), text fields, and buttons for modifying the details of an
 * existing Part. The class allows users to save or cancel any modifications made to the Part's details.
 * @author Jonathan Lane
 */
public class ModifyPart implements Initializable {
    /** This RadioButton allows the user to specify that the Part they're modifying should be an InHouse Part. */
    public RadioButton inHouseRadio;
    /** This RadioButton allows the user to specify that the Part they're modifying should be an Outsourced Part. */
    public RadioButton outsourcedRadio;
    /** Displays the Part's ID, which is taken from the Part being modified */
    public TextField idField;
    /** Displays the Part's name, editable by the user */
    public TextField nameField;
    /** Displays the Product's stock, editable by the user */
    public TextField inventoryField;
    /** Displays the Part's price, editable by the user */
    public TextField priceField;
    /** Displays the Part's maximum stock, editable by the user */
    public TextField maxField;
    /** Displays the Part's minimum stock, editable by the user */
    public TextField minField;
    /**
     * Labels the Parts source. This can either be "Machine ID" if it's an InHouse Part, or "Company Name" if it's an
     * Outsourced Part.
     */
    public Label sourceLabel;
    /**
     * Displays the Parts source. This can either be an InHouse Part's Machine's ID, or and Outsourced Part's Company
     * Name.
     */
    public TextField sourceField;
    /** The Part that is being referenced, it is assigned the Part that is selected from the Main Screen. */
    private Part partToModify = null;

    /**
     * This method is called by the FXMLLoader when the modify-part.fxml file is loaded. It sets the initial values
     * for all the fields in the screen. If the product being modified no longer exists, an error message is displayed
     * and the user is redirected to the main screen. This also sets the sourceLabel to "Machine ID" or "Company Name"
     * depending on if the Part is InHouse or Outsourced.
     * @param url The URL used to initialize the controller.
     * @param resourceBundle The ResourceBundle used to initialize the controller.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("ModifyPart Controller Initialized");
        // partToModify needs to be set to a Part that is selected within MainScreen.
        partToModify = InventoryApplication.partToModify;
        if (partToModify == null) {
            new Alert(AlertType.ERROR,
                    "The part you are modifying no longer exists! Please validate the data file before continuing to"
                            + " use this application!").showAndWait();
            InventoryApplication.toScreen(new ActionEvent(), InventoryApplication.ScreenType.MAIN_SCREEN);
            return;
        }
        idField.setText("" + partToModify.getId());
        nameField.setText(partToModify.getName());
        inventoryField.setText("" + partToModify.getStock());
        priceField.setText("" + partToModify.getPrice());
        maxField.setText("" + partToModify.getMax());
        minField.setText("" + partToModify.getMin());
        if (partToModify.getClass().getSimpleName().equals("InHouse")) {
            inHouseRadio.setSelected(true);
            sourceField.setText("" + ((InHouse)partToModify).getMachineId());
        }
        else if (partToModify.getClass().getSimpleName().equals("Outsourced")) {
            outsourcedRadio.setSelected(true);
            sourceLabel.setText("Company Name"); // Default text is for InHouse Parts
            sourceField.setText(((Outsourced)partToModify).getCompanyName());
        }
    }

    /** This method is called when the inHouseRadio button is clicked, and it sets the sourceLabel to "Machine ID". */
    public void inHouseClicked() {
        sourceLabel.setText("Machine ID");
    }
    /** This method is called when the Outsourced button is clicked, and it sets the sourceLabel to "Company Name". */
    public void outsourcedClicked() {
        sourceLabel.setText("Company Name");
    }

    /**
     * LOGICAL ERROR
     * This method is called whenever the user clicks the "Save" button. There exist many try and catch blocks within
     * this method due to the need to check and notify the user of exactly what is wrong with their input, if anything.
     * Ultimately, this method attempts to obtain the user input for all the editable TextFields within this screen, and
     * parse that information into the data for the creation of a new Part (that is then added to Inventory).
     * Since this is the Modify Part screen, it will also remove the old Product from Inventory. It gets the new
     * Part's ID from InventoryApplication.nextPartId(), after removing the old Part. Upon successful modification
     * (technically recreation), it will direct the user back to the Main Screen.
     * <p><b>LOGICAL ERROR information:</b></p>
     * <p>
     *     When I modified a Part, I noticed that afterward, the data was malformed. I set up checks to ensure that Part
     *     IDs lined up with each other, that there were no duplicates, or missing IDs in the range.
     * </p>
     * <p>
     *     <b>The problem:</b>
     *     When I modified a part that wasn't the last ID in the Part ID range, it would generate the ID for the new
     *     Part as 1 (one) above the highest Part ID that existed, extending the range by 1 and leaving a gap in the
     *     Part IDs.
     * </p>
     * <p>
     *     <b>The solution:</b>
     *     I had already implemented a check for if the Part being modified doesn't exist, which deleted the part in the
     *     process. This used the "Inventory.deletePart(Part)" method, which returns a boolean depending on if it was
     *     successful or not.
     *     The problem was that I was creating the new Part object before the old Part had been deleted. I called
     *     Inventory.nextPartId() (custom method which returns the first positive unused Part ID) before the old Part was
     *     deleted. So I moved the new Part creation to after the old Part deletion, solving this issue.
     * </p>
     * @param event The ActionEvent that is created from the Save button within the scene
     */
    public void saveButtonClicked(ActionEvent event) {
        try {
            String name = nameField.getText();
            if (name.equals("")) {
                throw new IllegalArgumentException("Name field must not be blank!");
            }
            double price;
            int stock;
            int min;
            int max;
            try { price = Double.parseDouble(priceField.getText()); } catch (NumberFormatException e) {
                throw new NumberFormatException("Price field must be of type Double! Found \""
                        + (priceField.getText().equals("") ? "[Empty]" : priceField.getText()) + "\".");
            }
            try { stock = Integer.parseInt(inventoryField.getText()); } catch (NumberFormatException e) {
                throw new NumberFormatException("Inventory field must be of type Integer! Found \""
                        + (inventoryField.getText().equals("") ? "[Empty]" : inventoryField.getText()) + "\".");
            }
            try { min = Integer.parseInt(minField.getText()); } catch (NumberFormatException e) {
                throw new NumberFormatException("Min field must be a positive Integer! Found \""
                        + (minField.getText().equals("") ? "[Empty]" : minField.getText()) + "\".");
            }
            try { max = Integer.parseInt(maxField.getText()); } catch (NumberFormatException e) {
                throw new NumberFormatException("Max field must be a positive Integer greater than Min! Found \""
                        + (maxField.getText().equals("") ? "[Empty]" : maxField.getText()) + "\".");
            }

            // Validate stock is between min and max, and that min and max are valid
            if (min > max) {
                throw new IllegalArgumentException("Invalid min and/or max values! Min should be less than max!");
            }
            if (min < 0 || stock < 0) {
                throw new IllegalArgumentException("Min, Max, and Inventory must be positive!");
            }
            if (stock < min || stock > max) {
                throw new IllegalArgumentException("Invalid stock value is not between min and max values: " + stock);
            }

            if (inHouseRadio.isSelected()) {
                int machineId;
                try { machineId = Integer.parseInt(sourceField.getText()); } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Machine ID field must be of type Integer! Found " +
                            sourceField.getText());
                }
                // We are removing the part from Inventory to free up the ID, but it's still accessible from this class.
                if (!Inventory.deletePart(partToModify)) {
                    throw new ArrayIndexOutOfBoundsException("Part isn't found in inventory!");
                }
                InHouse newPart = new InHouse(InventoryApplication.nextPartId(), name, price, stock, min, max, machineId);
                Inventory.addPart(newPart);
            }
            else if (outsourcedRadio.isSelected()) {
                String companyName = sourceField.getText();
                if (companyName.equals("")) {
                    throw new IllegalArgumentException("Company Name field must not be blank!");
                }
                // We are removing the part from Inventory to free up the ID, but it's still accessible from this class.
                if (!Inventory.deletePart(partToModify)) {
                    throw new ArrayIndexOutOfBoundsException("Part isn't found in inventory!");
                }
                Outsourced newPart = new Outsourced(InventoryApplication.nextPartId(), name, price, stock, min, max, companyName);
                Inventory.addPart(newPart);
            }
        }
        catch (NumberFormatException e) {
            System.err.println("User input in ModifyPart form does not match needed types! " + e.getMessage());
            Alert badInputAlert = new Alert(AlertType.ERROR,
                    "One or more fields are either empty or " +
                            "have invalid input! Please correct the input before submitting!\n\n" + e.getMessage());
            badInputAlert.showAndWait();
            return;
        }
        catch (IllegalArgumentException e) {
            System.err.println("User input for Part's stock is invalid! " + e.getMessage());
            Alert badInputAlert = new Alert(Alert.AlertType.ERROR,
                    "Some input is invalid! Please correct the input before submitting." +
                            "\n\n" + e.getMessage());
            badInputAlert.showAndWait();
            return;
        }
        catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("Tried to modify a part that doesn't exist, even though part was added from reference!\n"
                    + e.getMessage() + "\n");
            e.printStackTrace();
            Alert badInputAlert = new Alert(AlertType.ERROR,
                    "The part you are modifying no longer exists! Please validate the data file before continuing to"
                            + "use this application!\n\n" + e.getMessage());
            badInputAlert.showAndWait();
            return;
        }
        // Successful modification
        InventoryApplication.partToModify = null;
        InventoryApplication.toScreen(event, InventoryApplication.ScreenType.MAIN_SCREEN);
    }

    /**
     * This method is called whenever the user clicks the "Cancel" button. It simply directs the user back to the Main
     * Screen, without saving any info that may have been created in the other actions of the scene. It also sets the
     * InventoryApplication's static partToModify object to null before returning to ensure consistency.
     * @param event The ActionEvent that is created from the Cancel button within the scene
     */
    public void cancelButton(ActionEvent event) {
        InventoryApplication.partToModify = null;
        InventoryApplication.toScreen(event, InventoryApplication.ScreenType.MAIN_SCREEN);
    }
}
