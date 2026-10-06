public class WishlistView
{
    private final String productID;
    private final String productName;
    private final double price;
    private final int wishlistQuantity;
    private final int stock;

    public WishlistView(String productID, String productName, double price,
                        int wishlistQuantity, int stock)
    {
        this.productID = productID;
        this.productName = productName;
        this.price = price;
        this.wishlistQuantity = wishlistQuantity;
        this.stock = stock;
    }

    public String getProductID()       { return productID; }
    public String getProductName()     { return productName; }
    public double getPrice()           { return price; }
    public int getWishlistQuantity()   { return wishlistQuantity; }
    public int getStock()              { return stock; }
}