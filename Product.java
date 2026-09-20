public class Product 
{
    private static int nextID = 2000;

    private final String productID;     // Immutable
    private String name;
    private double price;
    private int stock;

    // New product — Product generates its own ID.
    public Product(String name, double price, int stock) 
    {
        this.productID = "P" + (++nextID);
        this.name = name;
        this.price = price;
        this.stock = stock;
    }

    // Rebuild a product that already has an ID.
    public Product(String productID, String name, double price, int stock) 
    {
        this.productID = productID;
        this.name = name;
        this.price = price;
        this.stock = stock;
        syncCounter(productID);
    }

    private static void syncCounter(String id) 
    {
        try {
            int n = Integer.parseInt(id.replaceAll("\\D", ""));
            if (n > nextID) nextID = n;
        } catch (NumberFormatException ignored) { }
    }
    // Getters
    public String getName() 
    { 
        return name; 
    }
    public double getPrice() 
    { 
        return price; 
    }
    public int getStock() 
    { 
        return stock; 
    }
    public String getID() 
    { 
        return productID; 
    }
    // Setters
    public void setName(String name) 
    { 
        this.name = name; 
    }
    public void setPrice(double price) 
    { 
        this.price = price; 
    }
    public void setStock(int stock) 
    { 
        this.stock = stock; 
    }
}