/**
 * A single entry in a client's wishlist: the product the client wants,
 * paired with the quantity desired.
 */
public class WishlistItem 
{
    /** Id of the wished-for product; fixed once the entry is created. */
    private final String productID;

    /** How many units of the product the client wants; may be updated. */
    private int quantity;

    /**
     * Creates a wishlist entry.
     *
     * @param productID the id of the product the client wants
     * @param quantity  the number of units desired
     */
    public WishlistItem(String productID, int quantity) 
    {
        this.productID = productID;
        this.quantity = quantity;
    }

    /**
     * Returns the id of the wished-for product.
     *
     * @return the product id
     */
    public String getProductID() 
    { 
        return productID; 
    }

    /**
     * Returns the quantity the client currently wants.
     *
     * @return the desired quantity
     */
    public int getQuantity() 
    { 
        return quantity; 
    }

    /**
     * Updates the desired quantity for this entry.
     *
     * @param quantity the new desired quantity
     */
    public void setQuantity(int quantity) 
    { 
        this.quantity = quantity; 
    }
    
    /**
     * Returns a short human-readable description of this entry,
     * 
     * @return a string describing the product id and quantity
     */
    @Override
    public String toString() 
    {
        return "Product " + productID + ", qty " + quantity;
    }
}