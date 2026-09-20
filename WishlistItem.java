public class WishlistItem 
{
    private final String productID;
    private int quantity; // wishlistQuantity: how many the client wants

    public WishlistItem(String productID, int quantity) 
    {
        this.productID = productID;
        this.quantity = quantity;
    }

    public String getProductID() 
    { 
        return productID; 
    }
    public int getQuantity() 
    { 
        return quantity; 
    }
    public void setQuantity(int quantity) 
    { 
        this.quantity = quantity; 
    }
}