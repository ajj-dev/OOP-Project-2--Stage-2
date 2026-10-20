import java.util.ArrayList;
import java.util.List;

/**
 * Represents a client (customer) in the warehouse system.
 *
 * Each client has a unique, immutable client ID, a name, an address, a
 * wishlist, a waitlist, and a transaction history. Client IDs are generated
 * automatically with the prefix {@code "C"} followed by an
 * increasing number, starting at 1001.
 *
 * @author Aj Johnson, Aaditya Malla. Chidera Izuora
 * @version 1.0
 */
public class Client 
{
    /** The next numeric ID to assign to a newly created client. */
    private static int nextID = 1000;
    
    /** The unique, immutable identifier for this client. */
    private final String clientID;      // immutable
    
    /** The client's display name. */
    private String name;
    
    /** The client's mailing address. */
    private String address;
    
    /** The products this client wishes to purchase. */
    private final List<WishlistItem> wishlist = new ArrayList<>();
    
    /** The history of transactions (orders, invoices, payments) for this client. */
    private final List<Transaction> transactions = new ArrayList<>();

    // Client generates its own ID.
    
    /**
     * Constructs a new {@code Client} with an auto-generated client ID.
     *
     * @param name    the client's name
     * @param address the client's address
     */
    public Client(String name, String address) 
    {
        this.clientID = "C" + (++nextID);
        this.name = name;
        this.address = address;
    }
    
    /**
     * Reconstructs a {@code Client} that already has a client ID.
     *
     * Intended for restoring a client from persistent storage. The static
     * ID counter is advanced so that future auto-generated IDs never collide
     * with the provided ID.
     *
     * @param clientID the existing client ID
     * @param name     the client's name
     * @param address  the client's address
     */
    // Rebuild a client that already has an ID (load from save).
    public Client(String clientID, String name, String address) 
    {
        this.clientID = clientID;
        this.name = name;
        this.address = address;
        syncCounter(clientID);
    }
    /**
     * Advances the static ID counter so it stays ahead of any loaded client ID.
     *
     * @param id the client ID to synchronize against
     */
    // Keep the generator ahead of every ID we load, so new IDs never collide.
    private static void syncCounter(String id) 
    {
        try {
            int n = Integer.parseInt(id.replaceAll("\\D", ""));
            if (n > nextID) nextID = n;
        } catch (NumberFormatException ignored) { }
    }

    /**
     * Adds a single unit of the given product to the client's wishlist.
     *
     * @param productID the ID of the product to add
     * @return {@code true} if the entry was added or updated
     * @see #addWishlistEntry(String, int)
     */
    public boolean addWishlistEntry(String productID) { return addWishlistEntry(productID, 1); }
    /**
     * Adds the specified quantity of a product to the client's wishlist.
     *
     * If the product is already on the wishlist, its quantity is increased
     * by {@code quantity}; otherwise a new wishlist entry is created.
     *
     * @param productID the ID of the product to add
     * @param quantity  the number of units to add
     * @return {@code true} once the entry has been added or updated
     */
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
     /**
     * Returns the client's wishlist items.
     *
     * the live internal list,
     * not a copy.
     *
     * @return the list of {@link WishlistItem} objects on this client's wishlist
     */
    public List<WishlistItem> getWishListItems() 
    { 
        return wishlist; 
    }
    /**
     * Returns the client's name.
     *
     * @return the client's name
     */
    public String getName() 
    { 
        return name; 
    }
    /**
     * Returns the client's address.
     *
     * @return the client's address
     */
    public String getAddress() 
    { 
        return address; 
    }
    /**
     * Returns the client's unique ID.
     *
     * @return the client ID
     */
    public String getID() 
    { 
        return clientID; 
    }
    /**
     * Computes the client's current balance by summing all transaction amounts.
     *
     * @return the sum of all transaction amounts; {@code 0.0} if there are no
     *         transactions
     */
    public double getBalance() 
    { 
        double balance = 0.0;
        for (Transaction t : transactions) balance += t.getAmount();
        return balance;
    }
    
    // Setters
    /**
     * Sets the client's name.
     *
     * @param name the new name
     */
    public void setName(String name) 
    { 
        this.name = name; 
    }
    /**
     * Sets the client's address.
     *
     * @param address the new address
     */
    public void setAddress(String address) 
    { 
        this.address = address; 
    }
    
    /**
     * Sets the quantity of a wishlist entry. Does nothing if the product is
     * not on the wishlist.
     *
     * @param productID the ID of the product
     * @param quantity  the new quantity
     */
    public void setWishlistQuantity(String productID, int quantity)
    {
        for (WishlistItem w : wishlist)
            if (w.getProductID().equals(productID)) { w.setQuantity(quantity); return; }
    }
    /**
     * Returns the quantity of the given product on the client's wishlist.
     *
     * @param productID the ID of the product
     * @return the wishlist quantity, or {@code 0} if the product is not on the
     *         wishlist
     */
    public int getWishlistQuantity(String productID)
    {
        for (WishlistItem w : wishlist)
            if (w.getProductID().equals(productID)) return w.getQuantity();
        return 0;
    }
    /**
     * Removes the given product from the client's wishlist.
     *
     * @param productID the ID of the product to remove
     */
    public void removeWishlistEntry(String productID)
    {
        wishlist.removeIf(w -> w.getProductID().equals(productID));
    }

    // Account history: orders, invoices, payments.
    /**
     * Adds a transaction to the client's account history.
     *
     * @param t the transaction to record
     */
    public void addTransaction(Transaction t) { transactions.add(t); }
    /**
     * Returns a copy of all transactions for this client.
     *
     * @return a new {@link List} containing all of the client's transactions
     */
    public List<Transaction> getTransactions() { return new ArrayList<>(transactions); }

}