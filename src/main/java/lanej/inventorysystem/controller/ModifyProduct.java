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

/**
 * The ModifyProduct class is responsible for managing the user interface and functionality of the Modify Product
 * screen in the inventory management system. This class implements the Initializable interface from the JavaFX API.
 * It contains text fields, tables, and buttons for modifying the details of an existing product, including its
 * associated parts. The class allows users to search, add, and remove parts from the associated parts table, as well
 * as save and cancel any modifications made to the product's details.
 * @author Jonathan Lane
 */
public class ModifyProduct implements Initializable {
    /** Displays the Product's ID, which is taken from the Product being modified */
    public TextField idField;
    /** Displays the Product's name, editable by the user */
    public TextField nameField;
    /** Displays the Product's stock, editable by the user */
    public TextField inventoryField;
    /** Displays the Product's price, editable by the user */
    public TextField priceField;
    /** Displays the Product's maximum stock, editable by the user */
    public TextField maxField;
    /** Displays the Product's minimum stock, editable by the user */
    public TextField minField;
    /** A TableView of all the available Parts within Inventory */
    public TableView partTable;
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
    /** The search field for filtering partTable */
    public TextField partSearchField;
    /** A TableView of all the Parts associated with the Product */
    public TableView associatedTable;
    /** The Part ID column for associatedTable */
    public TableColumn associatedIdColumn;
    /** The Part name column for associatedTable */
    public TableColumn associatedNameColumn;
    /** The Part stock column for associatedTable */
    public TableColumn associatedInventoryColumn;
    /** The Part price column for associatedTable */
    public TableColumn associatedPriceColumn;
    /** The Part max column for associatedTable */
    public TableColumn associatedMaxColumn;
    /** The Part min column for associatedTable */
    public TableColumn associatedMinColumn;
    /** The search field for filtering associatedTable */
    public TextField associatedSearchField;

    /** The Product that is being added. It's necessary as a buffer in case the user decides to cancel the modification. */
    private final Product productToAdd = new Product(9999,"",0.0,0,0,0); // Must change later
    /** The Product that is being referenced, it is assigned the Product that is selected from the Main Screen. */
    private Product productToModify = null;
    /** The FilteredList of all available parts, which allows us to filter the items using the partSearchField */
    private FilteredList<Part> partFilteredList;
    /** The FilteredList of all associated parts, which allows us to filter the items using the associatedSearchField */
    private FilteredList<Part> associatedFilteredList;

    /**
     * This method is called by the FXMLLoader when the modify-product.fxml file is loaded. It sets the initial values
     * for all the fields in the screen, populates the parts table, and associated parts table with the parts that
     * are already associated with the product that is being modified. It also initializes the sorting and filtering
     * functionality for both the all parts table and the associated parts table. If the product being modified no
     * longer exists, an error message is displayed and the user is redirected to the main screen.
     * @param url The URL used to initialize the controller.
     * @param resourceBundle The ResourceBundle used to initialize the controller.
     */
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

    /**
     * This initializes and sorts the associatedFilteredList, and populates the associatedTable with it. This simplifies
     * my attempts to sort the associatedTable, as it would not automatically sort itself when a new Part association
     * is added. So to make it sort every time, I manually recreate the ObservableList that gets sorted before being
     * added to the associatedFilteredList every time that a Part association is added.
     */
    private void initSortedAssocParts() {
        ObservableList<Part> sortedAssociated = productToAdd.getAllAssociatedParts();
        sortedAssociated.sort(Comparator.comparingInt(Part::getId));
        associatedFilteredList = new FilteredList<>(sortedAssociated);
        associatedTable.setItems(associatedFilteredList);
        associatedTable.getSortOrder().add(associatedIdColumn);
        associatedTable.sort();
    }

    /**
     * This method is called when the user presses the "Add Part Association" button. It adds the Part association to
     * the Product object that would be added to Inventory if the user clicks "Save" afterward. If the same Part is
     * already associated with the Product for whatever reason, it asks the user to confirm before adding the duplicate
     * Part.
     */
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

    /**
     * This method is called whenever the user clicks the "Remove Part Association" button. It will ask the user for
     * confirmation before calling Product.deleteAssociatedPart(productToAdd). It only does this if the user has
     * selected an item within the Associated Parts table, otherwise it doesn't do anything.
     */
    public void removeButton() {
        if (associatedTable.getSelectionModel().getSelectedItem() != null) {
            Alert removeConfirmation = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to remove " +
                    ((Part)associatedTable.getSelectionModel().getSelectedItem()).getName() + "?");
            removeConfirmation.showAndWait();
            if (removeConfirmation.getResult() == ButtonType.OK) {
                productToAdd.deleteAssociatedPart((Part)associatedTable.getSelectionModel().getSelectedItem());
            }
        }
    }

    /**
     * This method is called whenever the user clicks the "Save" button. There exist many try and catch blocks within
     * this method due to the need to check and notify the user of exactly what is wrong with their input, if anything.
     * Ultimately, this method attempts to obtain the user input for all the editable TextFields within this screen
     * (not counting the search fields), and parse that information into the data for the creation of a new Product
     * (that is then added to Inventory).
     * Since this is the Modify Product screen, it will also remove the old Product from Inventory. It gets the new
     * Product's ID from the old Product. Upon successful modification (technically recreation), it will direct the
     * user back to the Main Screen.
     * @param event The ActionEvent that is created from the Save button within the scene
     */
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

    /**
     * This method is called whenever the user clicks the "Cancel" button. It simply directs the user back to the Main
     * Screen, without saving any info that may have been created in the other actions of the scene. It also sets the
     * InventoryApplication's static productToModify object to null before returning to ensure consistency.
     * @param event The ActionEvent that is created from the Cancel button within the scene
     */
    public void cancelButton(ActionEvent event) {
        InventoryApplication.productToModify = null;
        InventoryApplication.toScreen(event, InventoryApplication.ScreenType.MAIN_SCREEN);
    }
}
