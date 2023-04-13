package lanej.inventorysystem.controller;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import lanej.inventorysystem.InventoryApplication;
import lanej.inventorysystem.model.InHouse;
import lanej.inventorysystem.model.Inventory;
import lanej.inventorysystem.model.Outsourced;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * The AddPart class is responsible for managing the user interface and functionality of the Add Part
 * screen in the inventory management system. This class implements the Initializable interface from the JavaFX API.
 * It contains radio buttons (for selecting the Part type), text fields, and buttons for modifying the details of an
 * existing Part. The class allows users to save the Part's details to Inventory, or cancel, returning to the Main
 * Screen without returning anything.
 * @author Jonathan Lane
 */
public class AddPart implements Initializable {
    /** This RadioButton allows the user to specify that the Part they're adding should be an InHouse Part. */
    public RadioButton inHouseRadio;
    /** This RadioButton allows the user to specify that the Part they're adding should be an Outsourced Part. */
    public RadioButton outsourcedRadio;
    /** Displays the Part's ID, which is automatically generated, and not editable */
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

    /**
     * This method is called by the FXMLLoader when the add-part.fxml file is loaded. It sets the initial values
     * for the ID field, which includes the next available Part ID, and " - Generated" after it.
     * @param url The URL used to initialize the controller.
     * @param resourceBundle The ResourceBundle used to initialize the controller.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("AddPart Controller Initialized");
        idField.setText("" + InventoryApplication.nextPartId() + " – Generated");
    }

    /**
     * This method is called whenever the user clicks the "Cancel" button. It simply directs the user back to the Main
     * Screen, without saving any info that may have been created in the other actions of the scene.
     * @param event The ActionEvent that is created from the Cancel button within the scene
     */
    public void cancelButton(ActionEvent event) {
        InventoryApplication.toScreen(event, InventoryApplication.ScreenType.MAIN_SCREEN);
    }

    /**
     * This method is called whenever the user clicks the "Save" button. There exist many try and catch blocks within
     * this method due to the need to check and notify the user of exactly what is wrong with their input, if anything.
     * Ultimately, this method attempts to obtain the user input for all the editable TextFields within this screen, and
     * parse that information into the data for the creation of a new Part (that is then added to Inventory).
     * All information for the Part is sourced from the TextFields except for the ID, which is obtained from
     * InventoryApplication.nextPartId(). After creating the Part, it will direct the user back to the Main Screen.
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
                InHouse newPart = new InHouse(InventoryApplication.nextPartId(), name, price, stock, min, max, machineId);
                Inventory.addPart(newPart);
            } else if (outsourcedRadio.isSelected()) {
                String companyName = sourceField.getText();
                if (companyName.equals("")) {
                    throw new IllegalArgumentException("Company Name field must not be blank!");
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
            System.err.println("User input for Part is invalid! " + e.getMessage());
            Alert badInputAlert = new Alert(Alert.AlertType.ERROR,
                    "Some input is invalid! Please correct the input before submitting." +
                            "\n\n" + e.getMessage());
            badInputAlert.showAndWait();
            return;
        }
        // Successful addition
        InventoryApplication.toScreen(event, InventoryApplication.ScreenType.MAIN_SCREEN);
    }

    /** This method is called when the inHouseRadio button is clicked, and it sets the sourceLabel to "Machine ID". */
    public void inHouseClicked() {
        sourceLabel.setText("Machine ID");
    }

    /** This method is called when the Outsourced button is clicked, and it sets the sourceLabel to "Company Name". */
    public void outsourcedClicked() {
        sourceLabel.setText("Company Name");
    }
}
