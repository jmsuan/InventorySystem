package lanej.inventorysystem.model;

import javafx.collections.ObservableList;
import javafx.collections.FXCollections;

import java.util.LinkedList;

public class Product {
    private ObservableList<Part> associatedParts;   // Using ObservableList as it implements
                                                    // listener methods for changes within the GUI.
    private int id;         // Part number identifier
    private String name;    // Human readable part identifier
    private double price;   // Cost of the part
    private int stock;      // How many of each product is available
    private int min;        // Minimum number we must hold of the product
    private int max;        // Maximum number we can store of the part

    // Default constructor
    public Product(int id, String name, double price, int stock, int min, int max) {
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
    public void setId(int id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setPrice(double price) {
        this.price = price;
    }
    public void setStock(int stock) {
        this.stock = stock;
    }
    public void setMin(int min) {
        this.min = min;
    }
    public void setMax(int max) {
        this.max = max;
    }

    // Getter methods
    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public double getPrice() {
        return price;
    }
    public int getStock() {
        return stock;
    }
    public int getMin() {
        return min;
    }
    public int getMax() {
        return max;
    }

    public void addAssociatedPart(Part part) {
        associatedParts.add(part);
    }

    public boolean deleteAssociatedPart(Part selectedAssociatedPart) {
        if (associatedParts.contains(selectedAssociatedPart)) {
            associatedParts.remove(selectedAssociatedPart);
            return true;  // Part removal successful
        }
        else {
            return false; // Part removal error (not found)
        }
    }

    public ObservableList<Part> getAllAssociatedParts() {
        return associatedParts; // Returns direct reference to the Product instance's Part list.
    }
}
