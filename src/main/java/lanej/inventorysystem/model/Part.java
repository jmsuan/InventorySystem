package lanej.inventorysystem.model;

public abstract class Part {
    private int id;         // Part number identifier
    private String name;    // Human readable part identifier
    private double price;   // Cost of the part
    private int stock;      // How many of each part is available
    private int min;        // Minimum number we must hold of the part
    private int max;        // Maximum number we can store of the part

    Part(int id, String name, double price, int stock, int min, int max) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.min = min;
        this.max = max;
    }
}
