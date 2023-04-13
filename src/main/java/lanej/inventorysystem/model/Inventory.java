package lanej.inventorysystem.model;

import javafx.collections.ObservableList;
import javafx.collections.FXCollections;

/**
 * The Inventory class represents a collection of Parts and Products.
 * This class provides methods for adding, updating, deleting, and looking up Parts and Products.
 * The class uses ObservableList(s) to allow for dynamic changes to the list, which implements listener
 * methods to be used in updating the TableView list values.
 * @author Jonathan Lane
 */
public class Inventory {

    // Here we will use observableArrayList(s) due to an assumption that in this company,
    // inventory objects are likely to be edited often as opposed to being added/removed.
    // ObservableList(s) also implement listener methods for use in updating the TableView list values.
    /**
     * This ObservableList of Parts hold all the Parts associated with the program's Inventory. We are using an
     * observableArrayList here due to the unverified assumption that the company this software is for will likely be
     * editing Parts more often than adding and removing them. This will allow for faster access to each individual
     * Part.
     */
    private static final ObservableList<Part> allParts = FXCollections.observableArrayList();
    /**
     * This ObservableList of Products hold all the Products associated with the program's Inventory. We are using an
     * observableArrayList here due to the unverified assumption that the company this software is for will likely be
     * editing Products more often than adding and removing them. This will allow for faster access to each individual
     * Product.
     */
    private static final ObservableList<Product> allProducts = FXCollections.observableArrayList();

    /**
     * Adds a new Part to the Inventory.
     * @param newPart The Part object to be added
     */
    public static void addPart(Part newPart) {
        allParts.add(newPart);
    }

    /**
     * Adds a new Product to the Inventory.
     * @param newProduct The Product object to be added
     */
    public static void addProduct(Product newProduct) {
        allProducts.add(newProduct);
    }

    /**
     * Looks up a Part object in Inventory by its ID.
     * @param partId the ID of the Part object to look up.
     * @return the Part object found in Inventory, or null if not found.
     */
    public static Part lookupPart(int partId) {
        for (Part currentPart : allParts) {
            if (currentPart.getId() == partId) {
                return currentPart;
            }
        }
        return null; // Item not found
    }

    /**
     * Looks up a Product object in Inventory by its ID.
     * @param productId the ID of the Part object to look up.
     * @return the Product object found in Inventory, or null if not found.
     */
    public static Product lookupProduct(int productId) {
        for (Product currentProduct : allProducts) {
            if (currentProduct.getId() == productId) {
                return currentProduct;
            }
        }
        return null; // Item not found
    }

    /**
     * Updates a Part object in the Inventory at a specified index.
     * @param index the index of the Part object in the inventory to update.
     * @param selectedPart the updated Part object to replace the old Part object in the inventory.
     */
    public static void updatePart(int index, Part selectedPart) {
        allParts.set(index, selectedPart);
    }

    /**
     * Updates a Product object in the Inventory at a specified index.
     * @param index the index of the Product object in the inventory to update.
     * @param newProduct the updated Product object to replace the old Product object in the inventory.
     */
    public static void updateProduct(int index, Product newProduct) {
        allProducts.set(index, newProduct);
    }

    /**
     * Removes a Part object from the Inventory.
     * @param selectedPart the Part to be removed.
     * @return boolean value based on if removal was successful or not.
     */
    public static boolean deletePart(Part selectedPart) {
        if (allParts.contains(selectedPart)) {
            allParts.remove(selectedPart);
            return true;  // Part removal successful
        }
        else {
            return false; // Part removal error (not found)
        }
    }

    /**
     * Removes a Product object from the Inventory.
     * @param selectedProduct the Product to be removed.
     * @return boolean value based on if removal was successful or not.
     */
    public static boolean deleteProduct(Product selectedProduct) {
        if (allProducts.contains(selectedProduct)) {
            allProducts.remove(selectedProduct);
            return true;  // Product removal successful
        }
        else {
            return false; // Product removal error (not found)
        }
    }

    /**
     * Gets all the Parts which have been added to the Inventory.
     * @return ObservableList of all the Parts within the Inventory.
     */
    public static ObservableList<Part> getAllParts() {
        return allParts;
    }

    /**
     * Gets all the Products which have been added to the Inventory.
     * @return ObservableList of all the Products within the Inventory.
     */
    public static ObservableList<Product> getAllProducts() {
        return allProducts;
    }

}
