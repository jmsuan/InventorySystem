package lanej.inventorysystem.controller;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import lanej.inventorysystem.InventoryApplication;

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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("AddPart Controller Initialized");
    }

    public void cancelButton(ActionEvent event) {
        InventoryApplication.toScreen(event, InventoryApplication.ScreenType.MAIN_SCREEN);
    }

    public void saveButtonClicked(ActionEvent event) {

    }
}
