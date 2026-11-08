import java.util.ArrayList;
import java.util.List;

/**
 * Read-only display data for a product, tailored to a user's view.
 * @author Aj Johnson, Aaditya Malla. Chidera Izuora
 * @version 1.0
 */
public class ProductView
{
    private final String productID; // the id of the product being viewed
    private final String name; // the name of the product being viewed
    private final double price; // the price of the product being viewed
    private final int stock; // the number of units currently on hand for the product being viewed
    private final List<WaitlistItem> waitlist = new ArrayList<>();

    /**
     * Creates a customer-facing product view without stock information.
     *
     * @param productID the product ID
     * @param name the product name
     * @param price the product price
     */
    public ProductView(String productID, String name, double price)
    {
        this.productID = productID;
        this.name = name;
        this.price = price;
        this.stock = 0; // Default stock value for views
    }

    /**
     * Creates a clerk-facing product view.
     *
     * @param productID the product ID
     * @param name the product name
     * @param price the product price
     * @param stock the current stock
     */
    public ProductView(String productID, String name, double price, int stock)
    {
        this.productID = productID;
        this.name = name;
        this.price = price;
        this.stock = stock;
    }

    /**
     * Creates a product view including its waitlist.
     *
     * @param productID the product ID
     * @param name the product name
     * @param price the product price
     * @param stock the current stock
     * @param waitlist the waitlist entries to display
     */
    public ProductView(String productID, String name, double price, int stock, List<WaitlistItem> waitlist)
    {
        this.productID = productID;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.waitlist.addAll(waitlist);
    }

    /**
     * Formats this view for a client.
     *
     * @return the client-facing product description
     */
    public String clientString()
    {
        return String.format("ID=%s, Name=%s, Price=$%.2f", productID, name, price);
    }

    /**
     * Formats this view for a clerk.
     *
     * @return the clerk-facing product description
     */
    public String clerkString()
    {
        return String.format("ID=%s, Name=%s, Price=$%.2f, Stock=%d", productID, name, price, stock);
    }

    /**
     * Returns the product ID.
     * @return the product ID
     */
    public String getProductID()       { return productID; }
    /**
     * Returns the product name.
     * @return the product name
     */
    public String getName()            { return name; }
    /**
     * Returns the price.
     * @return the price
     */
    public double getPrice()           { return price; }
    /**
     * Returns the stock.
     * @return the stock
     */
    public int getStock()              { return stock; }

    /**
     * Returns the waitlist entries included in this view.
     *
     * @return the view's waitlist
     */
    public List<WaitlistItem> getWaitlist() { return waitlist; }
}
