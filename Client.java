import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

public class Client 
{
    private static int nextID = 1000;

    private final String clientID;      // immutable
    private String name;
    private String address;
    private final List<WishlistItem> wishlist = new ArrayList<>();
    private final List<Transaction> transactions = new ArrayList<>();
    private final List<WishlistItem> waitlist = new ArrayList<>();

    // Client generates its own ID.
    public Client(String name, String address) 
    {
        this.clientID = "C" + (++nextID);
        this.name = name;
        this.address = address;
    }

    // Rebuild a client that already has an ID (load from save).
    public Client(String clientID, String name, String address) 
    {
        this.clientID = clientID;
        this.name = name;
        this.address = address;
        syncCounter(clientID);
    }

    // Keep the generator ahead of every ID we load, so new IDs never collide.
    private static void syncCounter(String id) 
    {
        try {
            int n = Integer.parseInt(id.replaceAll("\\D", ""));
            if (n > nextID) nextID = n;
        } catch (NumberFormatException ignored) { }
    }

    public boolean addWishlistEntry(String productID) { return addWishlistEntry(productID, 1); }

    public boolean addWishlistEntry(String productID, int quantity) 
    {
        for (WishlistItem w : wishlist) 
        {
            if (w.getProductID().equals(productID)) 
            {
                w.setQuantity(w.getQuantity() + quantity);
                return true;
            }
        }
        wishlist.add(new WishlistItem(productID, quantity));
        return true;
    }
    // Getters
    public List<WishlistItem> getWishListItems() 
    { 
        return wishlist; 
    }
    public String getName() 
    { 
        return name; 
    }
    public String getAddress() 
    { 
        return address; 
    }
    public String getID() 
    { 
        return clientID; 
    }

    public double getBalance() 
    { 
        double balance = 0.0;
        for (Transaction t : transactions) balance += t.getAmount();
        return balance;
    }
    
    // Setters
    public void setName(String name) 
    { 
        this.name = name; 
    }
    public void setAddress(String address) 
    { 
        this.address = address; 
    }

    public void updateWishlistEntry(String productID, int quantity)
    {
        Iterator<WishlistItem> it = wishlist.iterator();
        while (it.hasNext())
        {
            WishlistItem w = it.next();
            if (w.getProductID().equals(productID))
            {
                if (quantity <= 0) it.remove();          // remove
                else w.setQuantity(quantity);            // change quantity (or no-op if same)
                return;
            }
        }
    }

    public void setWishlistQuantity(String productID, int quantity)
    {
        for (WishlistItem w : wishlist)
            if (w.getProductID().equals(productID)) { w.setQuantity(quantity); return; }
    }

    public int getWishlistQuantity(String productID)
    {
        for (WishlistItem w : wishlist)
            if (w.getProductID().equals(productID)) return w.getQuantity();
        return 0;
    }

    public void removeWishlistEntry(String productID)
    {
        wishlist.removeIf(w -> w.getProductID().equals(productID));
    }

    public void setWaitlistQuantity(String productID, int quantity)
    {
        for (WishlistItem w : waitlist)
            if (w.getProductID().equals(productID)) { w.setQuantity(quantity); return; }
    }

    public void removeWaitlistEntry(String productID)
    {
        waitlist.removeIf(w -> w.getProductID().equals(productID));
    }

    public int getWaitlistQuantity(String productID)
    {
        for (WishlistItem w : waitlist)
            if (w.getProductID().equals(productID)) return w.getQuantity();
        return 0;
    }

    public List<WishlistItem> getWaitlistItems() 
    { 
        return waitlist; 
    }

    // Account history: orders, invoices, payments.
    public void addTransaction(Transaction t) { transactions.add(t); }
    public List<Transaction> getTransactions() { return new ArrayList<>(transactions); }

}