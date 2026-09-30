import java.util.List;
import java.util.ArrayList;

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

    public List<WishlistItem> getClientWishlist(String clientID)
    {
        Client c = clientList.searchForClient(clientID);
        if (c == null) return null;
        return clientList.getClientWishlist(clientID);
    }

    public String createOrder(String clientID, List<OrderLine> lines)
    {
        Client c = clientList.searchForClient(clientID);
        if (c == null) return "Client not found.";

        double total = 0.0;
        StringBuilder report = new StringBuilder();
        List<String> toRemove = new ArrayList<>(); // productIDs to drop from the wishlist
        List<OrderLine> toDecrement = new ArrayList<>(); // items to reduce, not remove

        for (OrderLine line : lines)
        {
            String pid = line.getProductID();
            int ordered = line.getQuantity();

            // Find the wishlist quantity for this item so we know remove vs decrement.
            int wishQty = c.getWishlistQuantity(pid); // small helper on Client, 0 if absent

            if (ordered <= 0)
            {
                toRemove.add(pid); // not ordered -> remove
                continue;
            }

            // Fill against stock, waitlist the shortfall, debit.
            Product p = catalog.searchForProduct(pid);
            if (p == null) continue;
            int filled = Math.min(ordered, p.getStock());
            int shortfall = ordered - filled;
            if (filled > 0) { p.setStock(p.getStock() - filled); total += filled * p.getPrice();
                report.append("Filled ").append(filled).append(" x ").append(p.getName()).append("\n"); }
            if (shortfall > 0) { p.addToWaitlist(clientID, shortfall);
                report.append("Waitlisted ").append(shortfall).append(" x ").append(p.getName()).append("\n"); }

            // Wishlist bookkeeping: full order clears it, partial reduces it.
            if (ordered >= wishQty) toRemove.add(pid);
            else toDecrement.add(new OrderLine(pid, wishQty - ordered));
        }

        // Apply all wishlist changes after the loop
        for (String pid : toRemove)        c.removeWishlistEntry(pid);
        for (OrderLine d : toDecrement)    c.setWishlistQuantity(d.getProductID(), d.getQuantity());

        if (total > 0) c.addTransaction(new Transaction("ORDER", "Order placed", -total));
        report.append(String.format("Total debited: $%.2f", total));
        return report.toString();
    }

    public String receiveShipment(List<ShipmentLine> shipmentLines)
    {
        StringBuilder report = new StringBuilder();
        for (ShipmentLine line : shipmentLines)
        {
            String productID = line.getProductID();
            int quantityReceived = line.getQuantity();
            Product p = catalog.searchForProduct(productID);
            if (p == null)
            {
                report.append("Product not found: ").append(productID).append("\n");
                continue;
            }

            int remaining = quantityReceived;
            List<WaitlistItem> satisfied = new ArrayList<>(); // remove after the loop

            for (WaitlistItem entry : p.getWaitlist()) // FIFO
            {
                if (remaining <= 0) break; // shipment exhausted, rest stay waiting

                int wanted = entry.getQuantity();
                int filled = Math.min(wanted, remaining);         
                remaining -= filled;

                // Waiting client for what we just filled.
                Client c = clientList.searchForClient(entry.getClientID());
                if (c != null)
                {
                    double cost = filled * p.getPrice();
                    c.addTransaction(new Transaction("ORDER", "Waitlist filled: " + p.getName(), -cost));
                }
                report.append("Filled ").append(filled).append(" for ").append(entry.getClientID()).append("\n");

                if (filled >= wanted) satisfied.add(entry); // fully fulfilled -> remove
                else entry.setQuantity(wanted - filled); // partially fulfilled -> reduce, stays waiting
            }
        p.getWaitlist().removeAll(satisfied); // apply removals after iterating
        p.setStock(p.getStock() + remaining);   
        report.append("Added ").append(remaining).append(" to stock of ")
      .append(p.getName()).append(". New stock: ").append(p.getStock()).append("\n"); // leftover after waitlist -> stock
        }
        return report.toString();
    }

    public String receivePayment(String clientID, double amount)
    {
        Client c = clientList.searchForClient(clientID);
        if (c == null) return "Client not found.";
        if (amount <= 0) return "Payment must be positive.";

        c.addTransaction(new Transaction("PAYMENT", "Payment received", amount)); // positive == credit
        return String.format("Payment of $%.2f recorded for %s. New balance: $%.2f",
                amount, c.getName(), c.getBalance());
    }
}