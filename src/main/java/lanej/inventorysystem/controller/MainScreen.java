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

/**
 * The MainScreen class is responsible for managing the user interface and functionality of the main (landing) screen in
 * the inventory management system. This class implements the Initializable interface from the JavaFX API. It contains
 * tabular views, search fields, labels, and buttons for navigation within the program or other actions defined within
 * the Class. The class allows users to view information for, or delete, the various Parts and Products within the
 * "Inventory". It also allows the user to navigate to the appropriate scenes/pages for adding/modifying Parts and or
 * Products.
 * @author Jonathan Lane
 */
public class MainScreen implements Initializable {
    /** The TableView in which all the Parts within Inventory are displayed */
    public TableView<Part> partTable;
    /** The TableView in which all the Products within Inventory are displayed */
    public TableView<Product> productTable;
    /** The Part ID column for partTable */
    public TableColumn partIdColumn;
    /** The Part name column for partTable */
    public TableColumn partNameColumn;
    /** The Part stock column for partTable */
    public TableColumn partInventoryColumn;
    /** The Part price column for partTable */
    public TableColumn partPriceColumn;
    /** The Part max column for partTable */
    public TableColumn partMaxColumn;
    /** The Part min column for partTable */
    public TableColumn partMinColumn;
    /** The Product ID column for productTable */
    public TableColumn productIdColumn;
    /** The Product name column for productTable */
    public TableColumn productNameColumn;
    /** The Product stock column for productTable */
    public TableColumn productInventoryColumn;
    /** The Product price column for productTable */
    public TableColumn productPriceColumn;
    /** The Product max column for productTable */
    public TableColumn productMaxColumn;
    /** The Product min column for productTable */
    public TableColumn productMinColumn;
    /** The search field for filtering partTable */
    public TextField partSearchField;
    /** The search field for filtering productTable */
    public TextField productSearchField;
    /** The FilteredList of all Parts within Inventory, which allows us to filter the items using the partSearchField */
    private FilteredList<Part> partFilteredList;
    /** The FilteredList of all Products within Inventory, which allows us to filter the items using the productSearchField */
    private FilteredList<Product> productFilteredList;

    /**
     * This method is called by the FXMLLoader when the main-screen.fxml file is loaded. It populates the parts and
     * product tables. It also initializes the sorting and filtering functionality for both of the tables.
     * If the user tries a search String that doesn't match any Parts/Products, a warning alert is shown to the user.
     * @param url The URL used to initialize the controller.
     * @param resourceBundle The ResourceBundle used to initialize the controller.
     */
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

    /**
     * This method is called whenever the user clicks the "Add" button under the Parts table. It simply directs the user
     * to the Add Part Screen using the InventoryApplication.toScreen(ActionEvent, ScreenType) method.
     * @param event The ActionEvent that is created from the Add (Part) button within the scene
     */
    public void partAddButton(ActionEvent event) {
        InventoryApplication.toScreen(event, InventoryApplication.ScreenType.ADD_PART);
    }
    /**
     * This method is called whenever the user clicks the "Add" button under the Products table. It simply directs the
     * user to the Add Product Screen using the InventoryApplication.toScreen(ActionEvent, ScreenType) method.
     * @param event The ActionEvent that is created from the Add (Product) button within the scene
     */
    public void productAddButton(ActionEvent event) {
        InventoryApplication.toScreen(event, InventoryApplication.ScreenType.ADD_PRODUCT);
    }

    /**
     * This method is called whenever the user clicks the "Modify" button under the Parts table. It first confirms that
     * the user has already selected an item within the Parts table, then sets the InventoryApplication.partToModify
     * object to refer to that item, then directs the user to the Modify Part Screen using the
     * InventoryApplication.toScreen(ActionEvent, ScreenType) method.
     * @param event The ActionEvent that is created from the Modify (Part) button within the scene
     */
    public void partModifyButton(ActionEvent event) {
        if (partTable.getSelectionModel().getSelectedItem() != null) {
            InventoryApplication.partToModify = partTable.getSelectionModel().getSelectedItem();
            InventoryApplication.toScreen(event, InventoryApplication.ScreenType.MODIFY_PART);
        }
    }
    /**
     * This method is called whenever the user clicks the "Modify" button under the Product table. It first confirms
     * that the user has already selected an item within the Products table, then sets the
     * InventoryApplication.productToModify object to refer to that item, then directs the user to the Modify Product
     * Screen using the InventoryApplication.toScreen(ActionEvent, ScreenType) method.
     * @param event The ActionEvent that is created from the Modify (Product) button within the scene
     */
    public void productModifyButton(ActionEvent event) {
        if (productTable.getSelectionModel().getSelectedItem() != null) {
            InventoryApplication.productToModify = productTable.getSelectionModel().getSelectedItem();
            InventoryApplication.toScreen(event, InventoryApplication.ScreenType.MODIFY_PRODUCT);
        }
    }

    /**
     * This method is called whenever the user clicks the "Delete" button under the Part table. It first confirms
     * that a Part is indeed selected in the Parts table, then checks if the Part is associated with any Products.
     * It will then show a confirmation alert to the user to confirm whether they really want to delete the Part. If
     * there are any Products that are associated with the Part, it will list these Products here.
     * If there were Product associations, but the user decides to delete the Part anyway, it will be added to the
     * InventoryApplication.pendingParts List.
     * @see lanej.inventorysystem.InventoryApplication InventoryApplication for information on the pendingParts list.
     */
    public void partDeleteButton() {
        Part selectedPart = partTable.getSelectionModel().getSelectedItem();
        if (selectedPart == null) {
            return;
        }
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
    /**
     * This method is called whenever the user clicks the "Delete" button under the Product table. It first confirms
     * that a Product is indeed selected in the Product table, then checks if the Product is associated with any Parts.
     * If it is, then an information alert is shown to the user, indicating that they need to delete all Part
     * associations before deleting the Product.
     * It will then show a confirmation alert to the user to confirm whether they really want to delete the Product.
     */
    public void productDeleteButton() {
        Product selectedProduct = productTable.getSelectionModel().getSelectedItem();
        if (selectedProduct == null) {
            return;
        }
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

    /**
     * This method is called whenever the user clicks the "Exit" button. It simply calls the
     * InventoryApplication.terminate(ActionEvent) method, which writes and changes that have been committed to the
     * designated data file for this program's instance, then closes the stage and application.
     * @param event The ActionEvent that is created from the Exit button within the scene
     */
    public void exitButton(ActionEvent event) {
        InventoryApplication.terminate(event);
    }
}
