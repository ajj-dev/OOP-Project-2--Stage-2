import java.util.ArrayList;
import java.util.List;
public class ProductView
{
    private final String productID; // the id of the product being viewed
    private final String name; // the name of the product being viewed
    private final double price; // the price of the product being viewed
    private final int stock; // the number of units currently on hand for the product being viewed
    private final List<WaitlistItem> waitlist = new ArrayList<>();

    /**
     * Creates a new ProductView object with the specified information.
     */
    public ProductView(String productID, String name, double price)
    {
        this.productID = productID;
        this.name = name;
        this.price = price;
        this.stock = 0; // Default stock value for views
    }

    public ProductView(String productID, String name, double price, int stock)
    {
        this.productID = productID;
        this.name = name;
        this.price = price;
        this.stock = stock;
    }

    public ProductView(String productID, String name, double price, int stock, List<WaitlistItem> waitlist)
    {
        this.productID = productID;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.waitlist.addAll(waitlist);
    }

    public String clientString()
    {
        return String.format("ID=%s, Name=%s, Price=$%.2f", productID, name, price);
    }

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

    public List<WaitlistItem> getWaitlist() { return waitlist; }
}