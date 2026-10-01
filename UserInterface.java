import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;

/**
 * Console-based menu for interacting with the warehouse system.
 *
 * <p>The interface presents a text-based command menu that allows a user to
 * create clients and products, manage wishlists, place orders, receive
 * shipments, and record client payments.</p>
 */
public class UserInterface 
{
    /** Warehouse instance that stores the system data. */
    private final Warehouse warehouse = new Warehouse();

    /** Console input source used by the text menu. */
    private final Scanner in = new Scanner(System.in);

    /**
     * Starts the command loop for the warehouse console interface.
     *
     * <p>Each menu option dispatches to a helper method for the corresponding
     * warehouse action until the user chooses to exit.</p>
     */
    public void run() 
    {
        while (true) 
        {
            System.out.println("\n1) Add client  2) Add product  3) Add to wishlist  "
                    + "4) Print clients  5) Print products  6) Print wishlist  0) Exit");
            System.out.print("Choose: ");
            String choice = in.nextLine().trim();
            switch (choice) 
            {
                case "1": addClient(); break;
                case "2": addProduct(); break;
                case "3": addToWishlist(); break;
                case "4": warehouse.printAllClients(); break;
                case "5": warehouse.printAllProducts(); break;
                case "6": printClientWishlist(); break;
                case "7": processClientOrder(); break;
                case "8": receiveShipment(); break;
                case "9": receivePayment(); break;
                case "0": return;
                default:  System.out.println("Invalid option.");
            }
        }
    }

    /**
     * Prompts the user to create a client and adds it to the warehouse.
     */
    public void addClient() 
    {
        System.out.print("Enter name: ");    String name = in.nextLine();
        System.out.print("Enter address: "); String address = in.nextLine();
        String id = warehouse.addClient(name, address);
        System.out.println("Client added: ID=" + id + ", Name=" + name + ", Address=" + address);
    }

    /**
     * Prompts the user for product details and adds a product to inventory.
     */
    public void addProduct() 
    {
        System.out.print("Enter name: ");  String name = in.nextLine();
        System.out.print("Enter price: "); double price = Double.parseDouble(in.nextLine());
        System.out.print("Enter stock: "); int stock = Integer.parseInt(in.nextLine());
        String id = warehouse.addProduct(name, price, stock);
        System.out.println("Product added: ID=" + id);
    }

    /**
     * Prompts the user to add a product to a client's wishlist.
     */
    public void addToWishlist() 
    {
        System.out.print("Enter client ID: ");  String clientID = in.nextLine();
        System.out.print("Enter product ID: "); String productID = in.nextLine();
        System.out.print("Enter quantity: ");   int qty = Integer.parseInt(in.nextLine());
        boolean ok = warehouse.addToWishlist(clientID, productID, qty);
        System.out.println(ok ? "Product added to wishlist." : "Error: client or product not found.");
    }

    /**
     * Displays the wishlist for a specific client.
     */
    public void printClientWishlist() 
    {
        System.out.print("Enter client ID: "); String clientID = in.nextLine();
        warehouse.printClientWishlist(clientID);
    }

    /**
     * Launches the user interface.
     *
     * @param args command-line arguments; not used by the program
     */
    public static void main(String[] args) 
    {
        new UserInterface().run();
    }

    /**
     * Walks a client through their wishlist and creates an order for selected items.
     */
    public void processClientOrder()
    {
        System.out.print("Enter client ID: "); 
        String clientID = in.nextLine();

        List<WishlistItem> wishlist = warehouse.getClientWishlist(clientID);
        if (wishlist == null) 
        {
            System.out.println("Client not found.");
            return;
        }
        if (wishlist.isEmpty()) 
        {
            System.out.println("Wishlist is empty.");
            return;
        }

        List<OrderLine> lines = new ArrayList<>();
        for (WishlistItem w : wishlist)
        {
            System.out.println(w);
            System.out.print("Order this item? (y/n): ");
            boolean order = in.nextLine().trim().equalsIgnoreCase("y");
            if (!order) continue;

            System.out.print("Enter quantity: ");
            int qty = Integer.parseInt(in.nextLine().trim());
            lines.add(new OrderLine(w.getProductID(), qty));
        }
        String summary = warehouse.createOrder(clientID, lines);
        System.out.println(summary);
    }

    /**
     * Collects shipment data from the console and submits it to the warehouse.
     */
    public void receiveShipment()
    {
        List<ShipmentLine> lines = new ArrayList<>();

        System.out.println("Enter shipment lines. Blank product ID when done.");
        while (true)
        {
            System.out.print("Product ID (blank to finish): ");
            String productID = in.nextLine().trim();
            if (productID.isEmpty()) break;

            System.out.print("Quantity received: ");
            int qty;
            try
            {
                qty = Integer.parseInt(in.nextLine().trim());
            }
            catch (NumberFormatException e)
            {
                System.out.println("  Not a number — line skipped.");
                continue;
            }
            if (qty <= 0)
            {
                System.out.println("  Quantity must be positive — line skipped.");
                continue;
            }

            lines.add(new ShipmentLine(productID, qty));
        }

        if (lines.isEmpty())
        {
            System.out.println("No shipment lines entered.");
            return;
        }

        String report = warehouse.receiveShipment(lines);
        System.out.println(report);
    }

    /**
     * Prompts for a client payment and records it in the warehouse.
     */
    public void receivePayment()
    {
        System.out.print("Enter client ID: ");
        String clientID = in.nextLine().trim();

        System.out.print("Enter payment amount: ");
        double amount;
        try
        {
            amount = Double.parseDouble(in.nextLine().trim());
        }
        catch (NumberFormatException e)
        {
            System.out.println("Not a valid amount.");
            return;
        }

        System.out.println(warehouse.receivePayment(clientID, amount));
    }
}