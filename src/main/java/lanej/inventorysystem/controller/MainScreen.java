package lanej.inventorysystem.controller;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import lanej.inventorysystem.InventoryApplication;
import lanej.inventorysystem.model.Inventory;
import lanej.inventorysystem.model.Part;
import lanej.inventorysystem.model.Product;

import java.net.URL;
import java.util.ResourceBundle;

public class MainScreen implements Initializable {
    public TableView<Part> partTable;
    public TableView<Product> productTable;
    public TableColumn partIdColumn;
    public TableColumn partNameColumn;
    public TableColumn partInventoryColumn;
    public TableColumn partPriceColumn;
    public TableColumn partMaxColumn;
    public TableColumn partMinColumn;
    public TableColumn productIdColumn;
    public TableColumn productNameColumn;
    public TableColumn productInventoryColumn;
    public TableColumn productPriceColumn;
    public TableColumn productMaxColumn;
    public TableColumn productMinColumn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Populate TableViews
        partIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInventoryColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        partMaxColumn.setCellValueFactory(new PropertyValueFactory<>("max"));
        partMinColumn.setCellValueFactory(new PropertyValueFactory<>("min"));
        partTable.setItems(Inventory.getAllParts());
        partTable.getSortOrder().add(partIdColumn);
        partTable.sort();
        productIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        productNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        productInventoryColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        productPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        productMaxColumn.setCellValueFactory(new PropertyValueFactory<>("max"));
        productMinColumn.setCellValueFactory(new PropertyValueFactory<>("min"));
        productTable.setItems(Inventory.getAllProducts());
        productTable.getSortOrder().add(productIdColumn);
        productTable.sort();

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
