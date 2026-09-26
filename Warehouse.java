import java.util.List;

public class Warehouse 
{
    private final ClientList clientList = new ClientList();
    private final Catalog catalog = new Catalog();

    public String addClient(String name, String address) 
    {
        Client c = new Client(name, address); // Client generates its own ID
        clientList.addClient(c);
        return c.getID();
    }

    public String addProduct(String name, double price, int stock) 
    {
        Product p = new Product(name, price, stock);
        catalog.addProduct(p);
        return p.getID();
    }

    public boolean addToWishlist(String clientID, String productID) 
    {
        return addToWishlist(clientID, productID, 1);
    }

    public boolean addToWishlist(String clientID, String productID, int quantity) 
    {
        if (clientList.searchForClient(clientID) == null) 
            return false;  // validate client
        if (catalog.searchForProduct(productID) == null) 
            return false;   // validate product
        return clientList.addClientWishlistEntry(clientID, productID, quantity);
    }

    public void printAllClients() 
    {
        List<Client> all = clientList.getAllClients();
        if (all.isEmpty()) 
        { 
            System.out.println("No clients available."); 
            return; 
        }
        for (Client c : all) 
        {
             System.out.println("ID=" + c.getID() + ", Name=" + c.getName() + ", Address=" + c.getAddress() + ", Balance=$" + String.format("%.2f", c.getBalance()));
        }
    }

    public void printAllProducts() 
    {
        List<Product> all = catalog.getAllProducts();
        if (all.isEmpty()) 
        { 
            System.out.println("No products available."); 
            return; 
        }
        for (Product p : all) 
        {
            System.out.println("ID=" + p.getID() + ", Name=" + p.getName()
                    + ", Price=" + p.getPrice() + ", Stock=" + p.getStock());
        }
    }

    public void printClientWishlist(String clientID) 
    {
        List<WishlistItem> items = clientList.getClientWishlist(clientID);
        if (items == null) 
        { 
            System.out.println("Client not found."); 
            return; 
        }
        if (items.isEmpty()) 
        { 
            System.out.println("Wishlist is empty."); 
            return; 
        }
        for (WishlistItem w : items) 
        {
            Product p = catalog.searchForProduct(w.getProductID());
            if (p == null) continue;
            System.out.println("Name=" + p.getName() + ", Price=" + p.getPrice()
                    + ", WishlistQty=" + w.getQuantity() + ", Stock=" + p.getStock());
        }
    }

    // --- Waitlist (product-side): clients waiting on an out-of-stock product ---
    public boolean addToWaitlist(String productID, String clientID, int quantity)
    {
        Product p = catalog.searchForProduct(productID);
        if (p == null) return false;
        if (clientList.searchForClient(clientID) == null) return false;
        return p.addToWaitlist(clientID, quantity);
    }
    public List<WaitlistItem> getProductWaitlist(String productID)
    {
        Product p = catalog.searchForProduct(productID);
        return (p == null) ? null : p.getWaitlist();
    }
    public void printProductWaitlist(String productID)
    {
        List<WaitlistItem> wl = getProductWaitlist(productID);
        if (wl == null)      { System.out.println("Product not found."); return; }
        if (wl.isEmpty())    { System.out.println("Waitlist is empty."); return; }
        for (WaitlistItem w : wl)
            System.out.println("Client=" + w.getClientID() + ", WaitingQty=" + w.getQuantity());
    }

    // --- Transactions: dated history on a client account ---
    public boolean recordTransaction(String clientID, String type, String description, double amount)
    {
        Client c = clientList.searchForClient(clientID);
        if (c == null) return false;
        c.addTransaction(new Transaction(type, description, amount));
        return true;
    }
    public List<Transaction> getClientTransactions(String clientID)
    {
        Client c = clientList.searchForClient(clientID);
        return (c == null) ? null : c.getTransactions();
    }
    public void printClientTransactions(String clientID)
    {
        List<Transaction> ts = getClientTransactions(clientID);
        if (ts == null)   { System.out.println("Client not found."); return; }
        if (ts.isEmpty()) { System.out.println("No transactions."); return; }
        for (Transaction t : ts) System.out.println(t);
    }

}