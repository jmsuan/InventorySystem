package lanej.inventorysystem.model;

import javafx.collections.ObservableList;
import javafx.collections.FXCollections;

public class Inventory {

    // Here we will use observableArrayList(s) due to an assumption that in this company,
    // inventory objects are likely to be edited often as opposed to being added/removed.
    // ObservableList(s) also implement listener methods for use in updating the list values.
    private static ObservableList<Part> allParts = FXCollections.observableArrayList();
    private static ObservableList<Product> allProducts = FXCollections.observableArrayList();

    public static void addPart(Part newPart) {
        allParts.add(newPart);
    }

    public static void addProduct(Product newProduct) {
        allProducts.add(newProduct);
    }

    public static Part lookupPart(int partId) {
        return allParts.get(partId);
    }

    public static Product lookupProduct(int productId) {
        return allProducts.get(productId);
    }

    public static void updatePart(int index, Part selectedPart) {
        allParts.set(index, selectedPart);
    }

    public static void updateProduct(int index, Product newProduct) {
        allProducts.set(index, newProduct);
    }

    public static boolean deletePart(Part selectedPart) {
        if (allParts.contains(selectedPart)) {
            allParts.remove(selectedPart);
            return true;  // Part removal successful
        }
        else {
            return false; // Part removal error (not found)
        }
    }

    public static boolean deleteProduct(Product selectedProduct) {
        if (allProducts.contains(selectedProduct)) {
            allProducts.remove(selectedProduct);
            return true;  // Product removal successful
        }
        else {
            return false; // Product removal error (not found)
        }
    }

    public static ObservableList<Part> getAllParts() {
        return allParts;
    }

    public static ObservableList<Product> getAllProducts() {
        return allProducts;
    }

}