package lanej.inventorysystem.model;

/**
 * The Outsourced class represents a Part object that was sourced from an outside entity.
 * It extends the Part class and includes the additional companyName property.
 * @author Jonathan Lane
 */
public class Outsourced extends Part {
    /** The name of the company that provided this Part. */
    private String companyName;

    /**
     * Creates a new Outsourced Part with the specified attributes.
     * @param id The unique ID of the Part.
     * @param name The name of the Part.
     * @param price The price of the Part.
     * @param stock The current stock of the Part.
     * @param min The minimum stock level required for the Part.
     * @param max The maximum stock level allowed for the Part.
     * @param companyName The name of the company that provided the Part.
     */
    public Outsourced(int id, String name, double price, int stock, int min, int max, String companyName) {
        super(id, name, price, stock, min, max);
        this.companyName = companyName;
    }

    /**
     * Sets the name of the company that provided this Part.
     * @param companyName The name of the company.
     */
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    /**
     * Gets the name of the company that provided this Part.
     * @return The name of the company.
     */
    public String getCompanyName() {
        return companyName;
    }
}
