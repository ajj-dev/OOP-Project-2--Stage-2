/**
 * A view for displaying information about items in the waitlist.
 */
public class WaitlistView
{
    private final String clientID; // the id of the client on the waitlist
    private final String productID; // the id of the product for which the client is waiting
    private final int waitingQuantity; // the number of units the client is waiting for
    private final String productName; // the name of the product for which the client is waiting
    private final double price; // the price of the product for which the client is waiting
    private final int stock; // the number of units currently on hand for the product for which the client is waiting

    /**
     * Creates a new WaitlistView object with the specified information.
     */
    public WaitlistView(String clientID, String productID, int waitingQuantity,
                        String productName, double price, int stock)
    {
        this.clientID = clientID;
        this.productID = productID;
        this.waitingQuantity = waitingQuantity;
        this.productName = productName;
        this.price = price;
        this.stock = stock;
    }

    /**
     * Returns the client ID.
     * @return the client ID
     */
    public String getClientID()        { return clientID; }
    /**
     * Returns the product ID.
     * @return the product ID
     */
    public String getProductID()       { return productID; }
    /**
     * Returns the waiting quantity.
     * @return the waiting quantity
     */
    public int getWaitingQuantity()    { return waitingQuantity; }
    /**
     * Returns the product name.
     * @return the product name
     */
    public String getProductName()     { return productName; }
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
}