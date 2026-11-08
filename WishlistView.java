/**
 * Represents a view of a product in a client's wishlist, including the product's ID, name, price, the quantity desired by the client, and the current stock available.
 */
public class WishlistView
{
    private final String productID; // the id of the product
    private final String productName; // the name of the product
    private final double price; // the price of the product
    private final int wishlistQuantity; // the number of units the client wants to purchase
    private final int stock; // the number of units currently on hand for the product

    /**
     * Creates a view of one item in a client's wishlist.
     *
     * @param productID the product ID
     * @param productName the product name
     * @param price the product price
     * @param wishlistQuantity the quantity requested by the client
     * @param stock the current stock
     */
    public WishlistView(String productID, String productName, double price,
                        int wishlistQuantity, int stock)
    {
        this.productID = productID;
        this.productName = productName;
        this.price = price;
        this.wishlistQuantity = wishlistQuantity;
        this.stock = stock;
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
    public String getProductName()     { return productName; }
    /**
     * Returns the price.
     * @return the price
     */
    public double getPrice()           { return price; }
    /**
     * Returns the quantity of the product in the client's wishlist.
     * @return the wishlist quantity
     */
    public int getWishlistQuantity()   { return wishlistQuantity; }
    /**
     * Returns the stock.
     * @return the stock
     */
    public int getStock()              { return stock; }
}
