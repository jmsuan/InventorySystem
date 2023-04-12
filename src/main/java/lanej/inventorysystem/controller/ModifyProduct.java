package lanej.inventorysystem.controller;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import lanej.inventorysystem.InventoryApplication;
import lanej.inventorysystem.model.Inventory;
import lanej.inventorysystem.model.Part;
import lanej.inventorysystem.model.Product;

import java.net.URL;
import java.util.Comparator;
import java.util.ResourceBundle;

public class ModifyProduct implements Initializable {

    public TextField idField;
    public TextField nameField;
    public TextField inventoryField;
    public TextField priceField;
    public TextField maxField;
    public TextField minField;
    public TableView partTable;
    public TableColumn partIdColumn;
    public TableColumn partNameColumn;
    public TableColumn partInventoryColumn;
    public TableColumn partPriceColumn;
    public TableColumn partMaxColumn;
    public TableColumn partMinColumn;
    public TextField partSearchField;
    public TableView associatedTable;
    public TableColumn associatedIdColumn;
    public TableColumn associatedNameColumn;
    public TableColumn associatedInventoryColumn;
    public TableColumn associatedPriceColumn;
    public TableColumn associatedMaxColumn;
    public TableColumn associatedMinColumn;
    public TextField associatedSearchField;

    // We still need productToAdd in the case that the user clicks the Cancel button
    private final Product productToAdd = new Product(9999,"",0.0,0,0,0); // Must change later
    private Product productToModify = null;
    private FilteredList<Part> partFilteredList;
    private FilteredList<Part> associatedFilteredList;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("ModifyProduct Controller Initialized");
        // partToModify needs to be set to a Part that is selected within MainScreen.
        productToModify = InventoryApplication.productToModify;
        if (productToModify == null) {
            new Alert(Alert.AlertType.ERROR,
                    "The product you are modifying no longer exists! Please validate the data file before " +
                            "continuing to use this application!").showAndWait();
            InventoryApplication.toScreen(new ActionEvent(), InventoryApplication.ScreenType.MAIN_SCREEN);
            return;
        }
        idField.setText("" + productToModify.getId());
        nameField.setText(productToModify.getName());
        inventoryField.setText("" + productToModify.getStock());
        priceField.setText("" + productToModify.getPrice());
        maxField.setText("" + productToModify.getMax());
        minField.setText("" + productToModify.getMin());

        // Populate associated parts to display in TableView,
        // also because productToAdd will be the Product that replaces productToModify.
        for (Part currentPart : productToModify.getAllAssociatedParts()) {
            productToAdd.addAssociatedPart(currentPart);
        }

        // Populate All Parts TableView
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

        // Populate Associated Parts TableView
        associatedIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        associatedNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        associatedInventoryColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        associatedPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        associatedMaxColumn.setCellValueFactory(new PropertyValueFactory<>("max"));
        associatedMinColumn.setCellValueFactory(new PropertyValueFactory<>("min"));
        initSortedAssocParts();

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
            if (partFilteredList.isEmpty() && !searchText.equals("")) {
                Alert emptyAlert = new Alert(Alert.AlertType.WARNING, "No Parts found for the provided search " +
                        "criteria!");
                emptyAlert.showAndWait();
            }
        }));

        // Implement search functionality for every time there's a new value in associatedSearchField
        associatedSearchField.textProperty().addListener(((observableValue, oldValue, newValue) -> {
            String searchText = newValue.toLowerCase();
            associatedFilteredList.setPredicate(item -> {
                if (searchText.equals("")) {
                    return true;
                }
                if (searchText.matches("\\d+")) {
                    return item.getId() == Integer.parseInt(searchText);
                }
                else return item.getName().toLowerCase().contains(searchText); // Returns false if not met
            });
            if (associatedFilteredList.isEmpty() && !searchText.equals("")) {
                Alert emptyAlert = new Alert(Alert.AlertType.WARNING, "No Parts found for the provided search " +
                        "criteria!");
                emptyAlert.showAndWait();
            }
        }));
    }

    private void initSortedAssocParts() {
        ObservableList<Part> sortedAssociated = productToAdd.getAllAssociatedParts();
        sortedAssociated.sort(Comparator.comparingInt(Part::getId));
        associatedFilteredList = new FilteredList<>(sortedAssociated);
        associatedTable.setItems(associatedFilteredList);
        associatedTable.getSortOrder().add(associatedIdColumn);
        associatedTable.sort();
    }

    public void addButton() {
        if (partTable.getSelectionModel().getSelectedItem() != null) {
            // Get selected Part
            Part newAssociatedPart = (Part)partTable.getSelectionModel().getSelectedItem();

            // If Part is already associated, ask user to confirm
            if (productToAdd.getAllAssociatedParts().contains(newAssociatedPart)) {
                Alert duplicatePart = new Alert(Alert.AlertType.CONFIRMATION, "This Part is already " +
                        "associated with this Product!\nAre you sure you want to add this Part again?");
                duplicatePart.showAndWait();
                if (duplicatePart.getResult() == ButtonType.CANCEL) {
                    return;
                }
            }
            productToAdd.addAssociatedPart(newAssociatedPart);
            initSortedAssocParts();
        }
    }

    public void removeButton() {
        if (associatedTable.getSelectionModel().getSelectedItem() != null) {
            productToAdd.deleteAssociatedPart((Part)associatedTable.getSelectionModel().getSelectedItem());
        }
    }

    public void saveButton(ActionEvent event) {
        try {
            String name = nameField.getText();
            if (name.equals("")) {
                throw new IllegalArgumentException("Name field must not be blank!");
            }
            int stock;
            double price;
            int max;
            int min;
            try { stock = Integer.parseInt(inventoryField.getText()); } catch (NumberFormatException e) {
                throw new NumberFormatException("Inventory field must be of type Integer! Found \""
                        + (inventoryField.getText().equals("") ? "[Empty]" : inventoryField.getText()) + "\".");
            }
            try { price = Double.parseDouble(priceField.getText()); } catch (NumberFormatException e) {
                throw new NumberFormatException("Price field must be of type Double! Found \""
                        + (priceField.getText().equals("") ? "[Empty]" : priceField.getText()) + "\".");
            }
            try { max = Integer.parseInt(maxField.getText()); } catch (NumberFormatException e) {
                throw new NumberFormatException("Max field must be a positive Integer greater than Min! Found \""
                        + (maxField.getText().equals("") ? "[Empty]" : maxField.getText()) + "\".");
            }
            try { min = Integer.parseInt(minField.getText()); } catch (NumberFormatException e) {
                throw new NumberFormatException("Min field must be a positive Integer! Found \""
                        + (minField.getText().equals("") ? "[Empty]" : minField.getText()) + "\".");
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

            // Set proper values for the Product
            productToAdd.setName(name);
            productToAdd.setPrice(price);
            productToAdd.setStock(stock);
            productToAdd.setMin(min);
            productToAdd.setMax(max);
            productToAdd.setId(productToModify.getId());
            if (!Inventory.deleteProduct(productToModify)) {
                throw new ArrayIndexOutOfBoundsException("Product isn't found in inventory!");
            }
        }
        catch (IllegalArgumentException e) {
            System.err.println("User input in ModifyProduct is invalid! " + e.getMessage());
            Alert badInputAlert = new Alert(Alert.AlertType.ERROR,
                    "One or more fields have invalid input! Please correct the input before submitting!\n\n" + e.getMessage());
            badInputAlert.showAndWait();
            return;
        }
        catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("Tried to modify a product that doesn't exist, even though it was added from reference!\n"
                    + e.getMessage() + "\n");
            e.printStackTrace();
            Alert badInputAlert = new Alert(Alert.AlertType.ERROR,
                    "The product you are modifying no longer exists! Please validate the data file before continuing to"
                            + "use this application!\n\n" + e.getMessage());
            badInputAlert.showAndWait();
            return;
        }
        // Add to Inventory
        Inventory.addProduct(productToAdd);
        // Successful addition
        InventoryApplication.productToModify = null;
        InventoryApplication.toScreen(event, InventoryApplication.ScreenType.MAIN_SCREEN);
    }

    public void cancelButton(ActionEvent event) {
        InventoryApplication.toScreen(event, InventoryApplication.ScreenType.MAIN_SCREEN);
    }
}
