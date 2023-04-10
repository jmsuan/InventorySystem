package lanej.inventorysystem.controller;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
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
    }

    public void cancelButton(ActionEvent event) {
        InventoryApplication.toScreen(event, InventoryApplication.ScreenType.MAIN_SCREEN);
    }

    public void saveButtonClicked(ActionEvent event) {
        String name = nameField.getText();
        double price = Double.parseDouble(priceField.getText());
        int stock = Integer.parseInt(inventoryField.getText());
        int min = Integer.parseInt(minField.getText());
        int max = Integer.parseInt(maxField.getText());
        if (inHouseRadio.isSelected()) {
            int machineId = Integer.parseInt(sourceField.getText());
            InHouse newPart = new InHouse(InventoryApplication.nextPartId(), name, price, stock, min, max, machineId);
            Inventory.addPart(newPart);
        }
        else if (outsourcedRadio.isSelected()) {
            String companyName = sourceField.getText();
            Outsourced newPart = new Outsourced(InventoryApplication.nextPartId(), name, price, stock, min, max, companyName);
            Inventory.addPart(newPart);
        }
        InventoryApplication.toScreen(event, InventoryApplication.ScreenType.MAIN_SCREEN);
    }

    public void inHouseClicked() {
        sourceLabel.setText("Machine ID:");
    }

    public void outsourcedClicked() {
        sourceLabel.setText("Company:");
    }
}
