import java.util.ArrayList;
import java.util.List;

public class Product 
{
    private static int nextID = 2000;

    private final String productID;     // Immutable
    private String name;
    private double price;
    private int stock;
    private final List<WaitlistItem> waitlist = new ArrayList<>();

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

    // Waitlist: clients waiting for this product when stock is short.
    public boolean addToWaitlist(String clientID, int quantity)
    {
        for (WaitlistItem w : waitlist)
        {
            if (w.getClientID().equals(clientID))
            {
                w.setQuantity(w.getQuantity() + quantity);
                return true;
            }
        }
        waitlist.add(new WaitlistItem(clientID, quantity));
        return true;
    }
    public List<WaitlistItem> getWaitlist() { return new ArrayList<>(waitlist); }

}