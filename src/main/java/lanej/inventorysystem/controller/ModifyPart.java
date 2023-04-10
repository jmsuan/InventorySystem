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

public class ModifyPart implements Initializable {
    public RadioButton inHouseRadio;
    public RadioButton outsourcedRadio;
    public TextField idField;
    public TextField nameField;
    public TextField inventoryField;
    public TextField priceField;
    public TextField maxField;
    public TextField minField;
    public Label sourceLabel;
    public TextField sourceField;
    private Part partToModify = null;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // partToModify needs to be set to a Part that is selected within MainScreen.
        partToModify = InventoryApplication.partToModify;
        if (partToModify == null) {
            new Alert(AlertType.ERROR,
                    "The part you are modifying no longer exists! Please validate the data file before continuing to"
                            + "use this application!").showAndWait();
            InventoryApplication.toScreen(new ActionEvent(), InventoryApplication.ScreenType.MAIN_SCREEN);
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
            sourceLabel.setText("Company Name:"); // Default text is for InHouse Parts
            sourceField.setText(((Outsourced)partToModify).getCompanyName());
        }
        System.out.println("ModifyPart Controller Initialized");
    }

    public void inHouseClicked() {
        sourceLabel.setText("Machine ID:");
    }

    public void outsourcedClicked() {
        sourceLabel.setText("Company Name:");
    }

    public void saveButtonClicked(ActionEvent event) {
        try {
            String name = nameField.getText();
            double price = Double.parseDouble(priceField.getText());
            int stock = Integer.parseInt(inventoryField.getText());
            int min = Integer.parseInt(minField.getText());
            int max = Integer.parseInt(maxField.getText());
            if (inHouseRadio.isSelected()) {
                int machineId = Integer.parseInt(sourceField.getText());
                InHouse newPart = new InHouse(InventoryApplication.nextPartId(), name, price, stock, min, max, machineId);
                // We are removing the part from Inventory to free up the ID, but it's still accessible from this class.
                if (!Inventory.deletePart(partToModify)) {
                    throw new ArrayIndexOutOfBoundsException("Part isn't found in inventory!");
                }
                Inventory.addPart(newPart);
            }
            else if (outsourcedRadio.isSelected()) {
                String companyName = sourceField.getText();
                Outsourced newPart = new Outsourced(InventoryApplication.nextPartId(), name, price, stock, min, max, companyName);
                // We are removing the part from Inventory to free up the ID, but it's still accessible from this class.
                if (!Inventory.deletePart(partToModify)) {
                    throw new ArrayIndexOutOfBoundsException("Part isn't found in inventory!");
                }
                Inventory.addPart(newPart);
            }
        }
        catch (NumberFormatException e) {
            System.err.println("User Input in ModifyPart form does not match needed types! " + e.getMessage());
            Alert badInputAlert = new Alert(AlertType.ERROR,
                    "One or more fields have invalid input! Please correct the input before submitting!\n\n" + e.getMessage());
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

    public void cancelButton(ActionEvent event) {
        InventoryApplication.partToModify = null;
        InventoryApplication.toScreen(event, InventoryApplication.ScreenType.MAIN_SCREEN);
    }
}
