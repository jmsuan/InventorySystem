package lanej.inventorysystem.controller;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import lanej.inventorysystem.InventoryApplication;
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
    private FilteredList<Part> partFilteredList;
    private FilteredList<Product> productFilteredList;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("MainScreen Controller Initialized");

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
            // Checks if each item matches condition
            partFilteredList.setPredicate(item -> {
                if (searchText.equals("")) {
                    return true;
                }
                if (searchText.matches("\\d+")) {
                    return item.getId() == Integer.parseInt(searchText);
                }
                else return item.getName().toLowerCase().contains(searchText); // Returns false if not met
            });
            if (partFilteredList.isEmpty() && !searchText.equals("")) {
                Alert emptyAlert = new Alert(AlertType.WARNING, "No Parts found for the provided search criteria!");
                emptyAlert.showAndWait();
            }
        }));

        // Implement search functionality for every time there's a new value in productSearchField
        productSearchField.textProperty().addListener(((observableValue, oldValue, newValue) -> {
            String searchText = newValue.toLowerCase();
            // Checks if each item matches condition
            productFilteredList.setPredicate(item -> {
                if (searchText.equals("")) {
                    return true;
                }
                if (searchText.matches("\\d+")) {
                    return item.getId() == Integer.parseInt(searchText);
                }
                else return item.getName().toLowerCase().contains(searchText); // Returns false if not met
            });
            if (productFilteredList.isEmpty() && !searchText.equals("")) {
                Alert emptyAlert = new Alert(AlertType.WARNING, "No Products were found for the provided " +
                        "search criteria!");
                emptyAlert.showAndWait();
            }
        }));
    }

    public void partAddButton(ActionEvent event) {
        InventoryApplication.toScreen(event, InventoryApplication.ScreenType.ADD_PART);
    }
    public void productAddButton(ActionEvent event) {
        InventoryApplication.toScreen(event, InventoryApplication.ScreenType.ADD_PRODUCT);
    }

    public void partModifyButton(ActionEvent event) {
        if (partTable.getSelectionModel().getSelectedItem() != null) {
            InventoryApplication.partToModify = partTable.getSelectionModel().getSelectedItem();
            InventoryApplication.toScreen(event, InventoryApplication.ScreenType.MODIFY_PART);
        }
    }
    public void productModifyButton(ActionEvent event) {
        if (productTable.getSelectionModel().getSelectedItem() != null) {
            InventoryApplication.productToModify = productTable.getSelectionModel().getSelectedItem();
            InventoryApplication.toScreen(event, InventoryApplication.ScreenType.MODIFY_PRODUCT);
        }
    }

    public void partDeleteButton() {
        Part selectedPart = partTable.getSelectionModel().getSelectedItem();
        Alert badDelete = new Alert(AlertType.ERROR,
                "Unexpected error deleting this Part! Part does not exist in table.");

        // Find if part is associated with any Products, to warn user in the confirmation
        boolean foundDependency = false;
        StringBuilder dependencyProducts = new StringBuilder();
        for (Product product : Inventory.getAllProducts()) {
            for (Part associatedPart : product.getAllAssociatedParts()) {
                if (selectedPart == associatedPart) {
                    dependencyProducts.append("ID: ").append(associatedPart.getId()).append(" - ")
                            .append(associatedPart.getName()).append(" (is associated with) ")
                            .append(product.getName()).append("\n");
                    foundDependency = true;
                }
            }
        }

        // Delete confirmation
        String deleteString = "Are you sure you want to delete the \"" + selectedPart.getName() + "\" part? \n" +
                "This action is not reversible!" +
                (foundDependency ? "\n\nPart is associated with the following Products:\n" : "");
        Alert deleteConfirmation = new Alert(AlertType.CONFIRMATION, deleteString + dependencyProducts);
        deleteConfirmation.showAndWait();

        // Check confirmation value and show error if Part isn't found
        if (deleteConfirmation.getResult().equals(ButtonType.OK)) {
            if (Inventory.deletePart(selectedPart)) {
                System.out.println("Deleting Part: " + selectedPart.getName());
                InventoryApplication.rectifyPartIds();
                InventoryApplication.pendingParts.add(selectedPart);
            }
            else {
                badDelete.showAndWait();
            }
        }
    }
    public void productDeleteButton() {
        Product selectedProduct = productTable.getSelectionModel().getSelectedItem();
        int partsAssociated = selectedProduct.getAllAssociatedParts().size();
        if (partsAssociated > 0) {
            new Alert(AlertType.INFORMATION, "This Product can not be deleted!\n\n" +
                    partsAssociated + " parts are associated with it.\n\n" +
                    "Please remove these associations if you want to delete the Product.").showAndWait();
            return;
        }
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
                InventoryApplication.rectifyProductIds();
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
