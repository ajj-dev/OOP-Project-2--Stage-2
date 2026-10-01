/**
 * One line of a client's order: a product and the quantity the client wants to
 * order of it.
 *
 * <p>An immutable value object. The {@link UserInterface} builds a list of these
 * while walking the client through their wishlist, and passes the list to
 * {@link Warehouse#createOrder(String, java.util.List)}, which fills each line
 * from stock and waitlists any shortfall. Like the other line/entry classes, it
 * refers to the product by its id rather than holding a {@link Product} reference.</p>
 */
public class OrderLine
{
    /** Id of the product being ordered. */
    private final String productID;

    /** Number of units requested on this line. */
    private final int quantity;

    /**
     * Creates an order line.
     *
     * @param productID the id of the product being ordered
     * @param quantity  the number of units requested
     */
    public OrderLine(String productID, int quantity)
    {
        this.productID = productID;
        this.quantity = quantity;
    }

    /**
     * Returns the id of the product being ordered.
     *
     * @return the product id
     */
    public String getProductID() { return productID; }

    /**
     * Returns the number of units requested on this line.
     *
     * @return the requested quantity
     */
    public int getQuantity() { return quantity; }
}