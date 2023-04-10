package lanej.inventorysystem.controller;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import lanej.inventorysystem.InventoryApplication;
import lanej.inventorysystem.model.InHouse;
import lanej.inventorysystem.model.Inventory;
import lanej.inventorysystem.model.Part;
import lanej.inventorysystem.model.Product;

import java.net.URL;
import java.util.Comparator;
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
    public TextField partSearchField;
    public TextField productSearchField;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("MainScreen Controller Initialized");
        FilteredList<Part> partFilteredList;
        FilteredList<Product> productFilteredList;

        // Populate Part TableView
        partIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInventoryColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        partMaxColumn.setCellValueFactory(new PropertyValueFactory<>("max"));
        partMinColumn.setCellValueFactory(new PropertyValueFactory<>("min"));
        ObservableList<Part> sortedParts = Inventory.getAllParts();
        sortedParts.sort(Comparator.comparingInt(Part::getId));
        partFilteredList = new FilteredList<>(sortedParts);
        partTable.setItems(partFilteredList);
        partTable.getSortOrder().add(partIdColumn);
        partTable.sort();

        // Populate Product TableView
        productIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        productNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        productInventoryColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        productPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        productMaxColumn.setCellValueFactory(new PropertyValueFactory<>("max"));
        productMinColumn.setCellValueFactory(new PropertyValueFactory<>("min"));
        ObservableList<Product> sortedProducts = Inventory.getAllProducts();
        sortedProducts.sort(Comparator.comparingInt(Product::getId));
        productFilteredList = new FilteredList<>(sortedProducts);
        productTable.setItems(productFilteredList);
        productTable.getSortOrder().add(productIdColumn);
        productTable.sort();

        // Implement search functionality for every time there's a new value in partSearchField
        partSearchField.textProperty().addListener(((observableValue, oldValue, newValue) -> {
            String searchText = newValue.toLowerCase();
            partFilteredList.setPredicate(item -> {
                if (searchText.equals("")) {
                    return true;
                }
                if (searchText.matches("\\d+")) {
                    return item.getId() == Integer.parseInt(searchText);
                }
                else return item.getName().toLowerCase().contains(searchText); // Returns false if not met
            });
        }));

        // Implement search functionality for every time there's a new value in productSearchField
        productSearchField.textProperty().addListener(((observableValue, oldValue, newValue) -> {
            String searchText = newValue.toLowerCase();
            productFilteredList.setPredicate(item -> {
                if (searchText.equals("")) {
                    return true;
                }
                if (searchText.matches("\\d+")) {
                    return item.getId() == Integer.parseInt(searchText);
                }
                else return item.getName().toLowerCase().contains(searchText); // Returns false if not met
            });
        }));
    }

    public void partAddButton(ActionEvent event) {
        InventoryApplication.toScreen(event, InventoryApplication.ScreenType.ADD_PART);
    }
    public void productAddButton(ActionEvent event) {
        InventoryApplication.toScreen(event, InventoryApplication.ScreenType.ADD_PRODUCT);
    }

    public void partModifyButton(ActionEvent event) {
        InventoryApplication.partToModify = partTable.getSelectionModel().getSelectedItem();
        InventoryApplication.toScreen(event, InventoryApplication.ScreenType.MODIFY_PART);
    }
    public void productModifyButton(ActionEvent event) {
        InventoryApplication.productToModify = productTable.getSelectionModel().getSelectedItem();
        InventoryApplication.toScreen(event, InventoryApplication.ScreenType.MODIFY_PRODUCT);
    }

    public void partDeleteButton() {
        Part selectedPart = partTable.getSelectionModel().getSelectedItem();
        Alert badDelete = new Alert(AlertType.ERROR,
                "Unexpected error deleting this Part! Part does not exist in table.");

        // Delete confirmation
        Alert deleteConfirmation = new Alert(AlertType.CONFIRMATION,
                "Are you sure you want to delete the \"" + selectedPart.getName() + "\" part? \n" +
                        "This action is not reversible!");
        deleteConfirmation.showAndWait();

        // Check confirmation value and show error if Part isn't found
        if (deleteConfirmation.getResult().equals(ButtonType.OK)) {
            if (Inventory.deletePart(selectedPart)) {
                System.out.println("Deleting Part: " + selectedPart.getName());
            }
            else {
                badDelete.showAndWait();
            }
        }
    }
    public void productDeleteButton(ActionEvent event) {
        Product selectedProduct = productTable.getSelectionModel().getSelectedItem();
        Alert badDelete = new Alert(AlertType.ERROR,
                "Unexpected error deleting this Product! Product does not exist in table.");

        // Delete confirmation
        Alert deleteConfirmation = new Alert(AlertType.CONFIRMATION,
                "Are you sure you want to delete the \"" + selectedProduct.getName() + "\" Product? \n" +
                        "This action is not reversible!");
        deleteConfirmation.showAndWait();

        // Check confirmation value and show error if Part isn't found
        if (deleteConfirmation.getResult().equals(ButtonType.OK)) {
            if (Inventory.deleteProduct(selectedProduct)) {
                System.out.println("Deleting Product: " + selectedProduct.getName());
            }
            else {
                badDelete.showAndWait();
            }
        }
    }

    public void exitButton(ActionEvent event) {
        InventoryApplication.terminate(event);
    }
}
