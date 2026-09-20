import java.util.ArrayList;
import java.util.List;

public class ClientList 
{
    private final List<Client> clients = new ArrayList<>();

    public boolean addClient(Client client) 
    {
        if (searchForClient(client.getID()) != null) 
            return false; // uniqueness
        clients.add(client);
        return true;
    }

    public Client searchForClient(String clientID) 
    {
        for (Client c : clients) 
        {
            if (c.getID().equals(clientID)) 
                return c;
        }
        return null;
    }

    public boolean addClientWishlistEntry(String clientID, String productID) 
    {
        return addClientWishlistEntry(clientID, productID, 1);
    }

    public boolean addClientWishlistEntry(String clientID, String productID, int quantity) 
    {
        Client c = searchForClient(clientID);
        if (c == null) 
            return false;
        return c.addWishlistEntry(productID, quantity);
    }
    // Getters
    // Temporary copy.
    public List<Client> getAllClients() { return new ArrayList<>(clients); }

    public List<WishlistItem> getClientWishlist(String clientID) 
    {
        Client c = searchForClient(clientID);
        if (c == null) 
            return null;
        return c.getWishListItems();
    }
}