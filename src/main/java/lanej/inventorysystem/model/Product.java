package lanej.inventorysystem.model;

import javafx.collections.ObservableList;
import javafx.collections.FXCollections;

import java.util.LinkedList;

/**
 * Represents a product in the inventory management system. May be associated with various Parts.
 * @author Jonathan Lane
 * @see lanej.inventorysystem.model.Part
 */
public class Product {
    /**
     * Observable List of all the Parts which may are associated with this instance of Product. Using ObservableList as
     * it implements listener methods for changes, and reflects them in the GUI.
     */
    private final ObservableList<Part> associatedParts;   // Using ObservableList as it implements
                                                          // listener methods for changes within the GUI.
    /** The unique identifier for the Product. */
    private int id;         // Product number identifier
    /** The name of the Product. */
    private String name;    // Human readable part identifier
    /** The price of the Product, stored as a double. */
    private double price;   // Cost of the part
    /** The current stock of the Product within the company's Inventory */
    private int stock;      // How many of each product is available
    /** The minimum amount of stock that's required of the Product. */
    private int min;        // Minimum number we must hold of the product
    /** The maximum allowable stock being stored of the Product. */
    private int max;        // Maximum number we can store of the part

    /**
     * Constructs a new instance of Product. Opts to store the associatedParts as a ObservableList backed by a
     * LinkedList due to the fact that parts will be added and removed much more often than edited (within a Product).
     * @param id The product identifier.
     * @param name The name of the product.
     * @param price The price of the product.
     * @param stock The amount of stock of the product.
     * @param min The minimum amount of stock required for the product.
     * @param max The maximum amount of stock allowed for the product.
     */
    public Product(int id, String name, double price, int stock, int min, int max) {
        assert id > 0;
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.min = min;
        this.max = max;
        associatedParts = FXCollections.observableList(new LinkedList<>()); // Assuming part objects will be
                                                                            // added/removed more often than
                                                                            // edited (within Product).
    }

    // Setter methods
    /**Sets the ID of the Product.
     * @param id the ID of the Product */
    public void setId(int id) {
        assert id > 0;
        this.id = id;
    }
    /**Sets the name of the Product.
     * @param name the name of the Product */
    public void setName(String name) {
        this.name = name;
    }
    /**Sets the price of the Product.
     * @param price the price of the Product */
    public void setPrice(double price) {
        this.price = price;
    }
    /**Sets the stock of the Product.
     * @param stock the stock of the Product */
    public void setStock(int stock) {
        this.stock = stock;
    }
    /**Sets the minimum amount required of the Product.
     * @param min the minimum Product stock */
    public void setMin(int min) {
        this.min = min;
    }
    /**Sets the maximum amount allowed of the Product.
     * @param max the maxmimum Product stock */
    public void setMax(int max) {
        this.max = max;
    }

    // Getter methods
    /**Gets the ID of the Product.
     * @return the ID of the Product */
    public int getId() {
        return id;
    }
    /**Gets the name of the Product.
     * @return the name of the Product */
    public String getName() {
        return name;
    }
    /**Gets the price of the Product.
     * @return the price of the Product */
    public double getPrice() {
        return price;
    }
    /**Gets the stock of the Product.
     * @return the stock of the Product */
    public int getStock() {
        return stock;
    }
    /**Gets the minimum amount required of the Product.
     * @return the minimum stock of the Product */
    public int getMin() {
        return min;
    }
    /**Gets the maximum allowed amount of the Product.
     * @return the maximum stock of the Product */
    public int getMax() {
        return max;
    }

    /**
     * Adds a part to the list of parts associated with this product.
     * @param part The part to add.
     */
    public void addAssociatedPart(Part part) {
        associatedParts.add(part);
    }

    /**
     * Deletes a part from the list of parts associated with this product.
     * @param selectedAssociatedPart The part to delete.
     * @return boolean value based on whether Part deletion was successful.
     */
    public boolean deleteAssociatedPart(Part selectedAssociatedPart) {
        if (associatedParts.contains(selectedAssociatedPart)) {
            associatedParts.remove(selectedAssociatedPart);
            return true;  // Part removal successful
        }
        else {
            return false; // Part removal error (not found)
        }
    }

    /**
     * Returns an ObservableList of all parts associated with this product.
     * @return The list of associated parts.
     */
    public ObservableList<Part> getAllAssociatedParts() {
        return associatedParts; // Returns direct reference to the Product instance's Part list.
    }
}
