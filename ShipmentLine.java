public class ShipmentLine
{
    private final String productID;
    private final int quantity;

    public ShipmentLine(String productID, int quantity)
    {
        this.productID = productID;
        this.quantity = quantity;
    }

    public String getProductID() { return productID; }
    public int getQuantity() { return quantity; }
}