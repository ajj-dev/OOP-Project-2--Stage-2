public class OrderLine
{
    private final String productID;
    private final int quantity;

    public OrderLine(String productID, int quantity)
    {
        this.productID = productID;
        this.quantity = quantity;
    }

    public String getProductID() { return productID; }
    public int getQuantity() { return quantity; }
}