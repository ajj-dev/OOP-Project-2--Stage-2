public class WaitlistView
{
    private final String clientID;
    private final String productID;
    private final int waitingQuantity;
    private final String productName;
    private final double price;
    private final int stock;

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

    public String getClientID()        { return clientID; }
    public String getProductID()       { return productID; }
    public int getWaitingQuantity()    { return waitingQuantity; }
    public String getProductName()     { return productName; }
    public double getPrice()           { return price; }
    public int getStock()              { return stock; }
}