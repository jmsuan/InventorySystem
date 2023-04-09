package lanej.inventorysystem.model;

public abstract class Part {
    private int id;         // Part number identifier
    private String name;    // Human readable part identifier
    private double price;   // Cost of the part
    private int stock;      // How many of each part is available
    private int min;        // Minimum number we must hold of the part
    private int max;        // Maximum number we can store of the part

    // Default Constructor
    public Part(int id, String name, double price, int stock, int min, int max) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.min = min;
        this.max = max;
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
}
