import java.util.ArrayList;
import java.util.List;

/**
 * A product the warehouse sells, with its price, current stock level, and the
 * waitlist of clients waiting for it.
 *
 * <p>Each product carries a system-generated id of the form {@code P<n>} that
 * never changes once assigned. Ids are drawn from a counter shared by all
 * products, so every product created in a run gets a distinct id.</p>
 *
 * <p>The product owns its <em>waitlist</em>: when an order asks for more units
 * than are in stock, the shortfall is recorded here as a {@link WaitlistItem},
 * and a later shipment fills those entries in the order the clients joined.</p>
 * @author Aj Johnson, Aaditya Malla. Chidera Izuora
 * @version 1.0
 */
public class Product
{
    /** Last id number handed out; shared by all products so ids are unique. */
    private static int nextID = 2000;

    /** System-generated id, e.g. {@code P2001}; fixed once assigned. */
    private final String productID;

    /** Display name of the product. */
    private String name;

    /** Unit sale price in dollars. */
    private double price;

    /** Number of units currently on hand. */
    private int stock;

    /** Clients waiting for this product, in the order they joined. */
    private final List<WaitlistItem> waitlist = new ArrayList<>();

    /**
     * Creates a new product and assigns it the next available id.
     *
     * @param name  the product's display name
     * @param price the unit sale price in dollars
     * @param stock the number of units initially on hand
     */
    public Product(String name, double price, int stock)
    {
        this.productID = "P" + (++nextID);
        this.name = name;
        this.price = price;
        this.stock = stock;
    }

    /**
     * Re-creates a product that already has an id, for example when rebuilding
     * data that was saved earlier. Advances the shared id counter if necessary,
     * so products created afterward can never reuse this id.
     *
     * @param productID the existing product id (e.g. {@code "P2005"})
     * @param name      the product's display name
     * @param price     the unit sale price in dollars
     * @param stock     the number of units on hand
     */
    public Product(String productID, String name, double price, int stock)
    {
        this.productID = productID;
        this.name = name;
        this.price = price;
        this.stock = stock;
        syncCounter(productID);
    }

    /**
     * Moves the shared id counter past the numeric part of id, if that
     * number is higher than the counter. Ids with no usable number are ignored.
     *
     * @param id an existing product id whose number must not be handed out again
     */
    private static void syncCounter(String id)
    {
        try {
            int n = Integer.parseInt(id.replaceAll("\\D", ""));
            if (n > nextID) nextID = n;
        } catch (NumberFormatException ignored) { }
    }

    /**
     * Returns the product's display name.
     *
     * @return the name
     */
    public String getName()
    {
        return name;
    }

    /**
     * Returns the unit sale price.
     *
     * @return the price in dollars
     */
    public double getPrice()
    {
        return price;
    }

    /**
     * Returns the number of units currently on hand.
     *
     * @return the stock level
     */
    public int getStock()
    {
        return stock;
    }

    /**
     * Returns the product's system-generated id.
     *
     * @return the product id
     */
    public String getID()
    {
        return productID;
    }

    /**
     * Changes the product's display name.
     *
     * @param name the new name
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * Changes the unit sale price.
     *
     * @param price the new price in dollars
     */
    public void setPrice(double price)
    {
        this.price = price;
    }

    /**
     * Sets the number of units on hand, for example after an order is filled
     * or a shipment is received.
     *
     * @param stock the new stock level
     */
    public void setStock(int stock)
    {
        this.stock = stock;
    }

    /**
     * Records that a client is waiting for some units of this product. If the
     * client is already on the waitlist, the new quantity is added to their
     * existing entry (which keeps its original place in line); otherwise a new
     * entry is added at the end.
     *
     * @param clientID the id of the waiting client
     * @param quantity the number of additional units the client is waiting for
     * @return {@code true} once the request has been recorded
     */
    public boolean addToWaitlist(String clientID, int quantity)
    {
        for (WaitlistItem w : waitlist)
        {
            if (w.getClientID().equals(clientID))
            {
                w.setQuantity(w.getQuantity() + quantity);
                return true;
            }
        }
        waitlist.add(new WaitlistItem(clientID, quantity));
        return true;
    }
    /**
     * Removes the specified entries from the waitlist.
     *
     * @param entries the entries to remove
     */
    public void removeWaitlistEntries(List<WaitlistItem> entries)
    {
        waitlist.removeAll(entries);
    }

    /**
     * Returns a copy of the clients currently waiting for this product.
     *
     * @return the product's waitlist entries in FIFO order
     */
    public List<WaitlistItem> getWaitlist() { return new ArrayList<>(waitlist); }

}
