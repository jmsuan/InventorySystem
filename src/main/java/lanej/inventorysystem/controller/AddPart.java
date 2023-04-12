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

public class AddPart implements Initializable {
    public RadioButton inHouseRadio;
    public RadioButton outsourcedRadio;
    public ToggleGroup partSourceSelection;
    public TextField idField;
    public TextField maxField;
    public TextField minField;
    public TextField nameField;
    public TextField inventoryField;
    public TextField priceField;
    public TextField sourceField;
    public Label sourceLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("AddPart Controller Initialized");
        idField.setText("" + InventoryApplication.nextPartId() + " – Generated");
    }

    public void cancelButton(ActionEvent event) {
        InventoryApplication.toScreen(event, InventoryApplication.ScreenType.MAIN_SCREEN);
    }

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

    public void inHouseClicked() {
        sourceLabel.setText("Machine ID");
    }

    public void outsourcedClicked() {
        sourceLabel.setText("Company Name");
    }
}
