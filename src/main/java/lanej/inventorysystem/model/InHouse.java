package lanej.inventorysystem.model;

/**
 * The InHouse class represents a Part object that is manufactured in-house.
 * It extends the Part class and includes the additional machineId property.
 * @author Jonathan Lane
 */
public class InHouse extends Part {
    /** The ID of the machine that is used to manufacture this Part. */
    private int machineId;

    /**
     * Creates a new InHouse Part with the specified attributes.
     * @param id The unique ID of the Part.
     * @param name The name of the Part.
     * @param price The price of the Part.
     * @param stock The current stock of the Part.
     * @param min The minimum stock level required for the Part.
     * @param max The maximum stock level allowed for the Part.
     * @param machineId The ID of the machine used to manufacture the Part.
     */
    public InHouse(int id, String name, double price, int stock, int min, int max, int machineId) {
        super(id, name, price, stock, min, max);
        this.machineId = machineId;
    }

    /**
     * Sets the ID of the machine used to manufacture this Part.
     * @param id The ID of the machine.
     */
    public void setMachineId(int id) {
        this.machineId = id;
    }

    /**
     * Gets the ID of the machine used to manufacture this Part.
     * @return The ID of the machine.
     */
    public int getMachineId() {
        return machineId;
    }
}
