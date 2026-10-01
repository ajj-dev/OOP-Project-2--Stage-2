/**
 * One line of an incoming shipment: a product and the quantity received of it.
 *
 * <p>An immutable value object. The {@link UserInterface} collects a list of
 * these from the clerk and passes it to
 * {@link Warehouse#receiveShipment(java.util.List)}, which uses each line first
 * to fill the product's waitlist (in arrival order) and then adds any remaining
 * units to stock. The product is referenced by its id.</p>
 */
public class ShipmentLine
{
    /** Id of the product received. */
    private final String productID;

    /** Number of units received on this line. */
    private final int quantity;

    /**
     * Creates a shipment line.
     *
     * @param productID the id of the product received
     * @param quantity  the number of units received
     */
    public ShipmentLine(String productID, int quantity)
    {
        this.productID = productID;
        this.quantity = quantity;
    }

    /**
     * Returns the id of the product received.
     *
     * @return the product id
     */
    public String getProductID() { return productID; }

    /**
     * Returns the number of units received on this line.
     *
     * @return the received quantity
     */
    public int getQuantity() { return quantity; }
}