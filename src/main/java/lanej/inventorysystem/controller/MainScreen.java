package lanej.inventorysystem.controller;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
import lanej.inventorysystem.InventoryApplication;
import lanej.inventorysystem.model.Inventory;
import lanej.inventorysystem.model.Part;
import lanej.inventorysystem.model.Product;

import java.net.URL;
import java.util.ResourceBundle;

public class MainScreen implements Initializable {
    public TableView<Part> partTable;
    public TableView<Product> productTable;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Populate TableViews
        partTable.setItems(Inventory.getAllParts());
        productTable.setItems(Inventory.getAllProducts());

        System.out.println("MainScreen Controller Initialized");
    }

    public void partSearchField(ActionEvent event) {
    }

    public void partAddButton(ActionEvent event) {
        InventoryApplication.toScreen(event, InventoryApplication.ScreenType.ADD_PART);
    }

    public void partModifyButton(ActionEvent event) {
        InventoryApplication.toScreen(event, InventoryApplication.ScreenType.MODIFY_PART);
    }

    public void partDeleteButton(ActionEvent event) {
    }

    public void productSearchField(ActionEvent event) {
    }

    public void productAddButton(ActionEvent event) {
        InventoryApplication.toScreen(event, InventoryApplication.ScreenType.ADD_PRODUCT);
    }

    public void productModifyButton(ActionEvent event) {
        InventoryApplication.toScreen(event, InventoryApplication.ScreenType.MODIFY_PRODUCT);
    }

    public void productDeleteButton(ActionEvent event) {
    }

    public void exitButton(ActionEvent event) {
        InventoryApplication.terminate(event);
    }
}
