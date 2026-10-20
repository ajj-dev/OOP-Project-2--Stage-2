import java.util.ArrayList;
import java.util.List;

public class ClientState extends WarehouseState 
{
    private static ClientState instance;
    private final Warehouse warehouse = Warehouse.getInstance();

    private static final int LOGOUT = 0;
    private static final int CLIENT_DETAILS = 1;
    private static final int SHOW_PRODUCT_LIST = 2;
    private static final int SHOW_CLIENT_TRANSACTIONS = 3;
    private static final int ADD_TO_WISHLIST = 4;
    private static final int DISPLAY_CLIENT_WISHLIST = 5;
    private static final int PLACE_ORDER = 6;
    private static final int HELP = 7;

    private ClientState() {
        super();
    }

    public static ClientState getInstance() {
        if (instance == null) {
            instance = new ClientState();
        }
        return instance;
    }

    public void help() {
        System.out.println("Client State Help:");
        System.out.println("1. View Client Details");
        System.out.println("2. Show Product List");
        System.out.println("3. Show Client Transactions");
        System.out.println("4. Add to Wishlist");
        System.out.println("5. Display Client Wishlist");
        System.out.println("6. Place Order");
        System.out.println("0. Exit");
    }

    public void getClientDetails()
    {
        String userID = context.getUser();
        Client client = warehouse.getClient(userID);
        if (client != null) {
            System.out.println("Client ID: " + client.getID());
            System.out.println("Name: " + client.getName());
            System.out.println("Address: " + client.getAddress());
        } else {
            System.out.println("Client not found.");
        }
    }

    public void showProductList() {
        List<ProductView> productViews = warehouse.getProductListView(context.CLIENT);
        for (ProductView pv : productViews) {
            System.out.println(pv.clientString());
        }
    }

    public void showClientTransactions() {
        String userID = context.getUser();
        Client client = warehouse.getClient(userID);
        if (client != null) {
            List<Transaction> transactions = client.getTransactions();
            if (transactions.isEmpty()) {
                System.out.println("No transactions found for this client.");
            } else {
                for (Transaction t : transactions) {
                    System.out.println(t);
                }
            }
        } else {
            System.out.println("Client not found.");
        }
    }

    public void addItemToWishlist() 
    {
        String userID = context.getUser();
        System.out.print("Enter product ID: "); String productID = in.nextLine();
        System.out.print("Enter quantity: ");   int qty = Integer.parseInt(in.nextLine());
        boolean ok = warehouse.addToWishlist(userID, productID, qty);
        System.out.println(ok ? "Product added to wishlist." : "Error: client or product not found.");
    }

    public void displayClientWishlist() {
        String userID = context.getUser();
        Client client = warehouse.getClient(userID);
        if (client != null) {
            List<WishlistItem> wishlist = client.getWishListItems();
            if (wishlist.isEmpty()) {
                System.out.println("Wishlist is empty.");
            } else {
                for (WishlistItem item : wishlist) {
                    System.out.println(item);
                }
            }
        } else {
            System.out.println("Client not found.");
        }
    }

    public void placeOrder() {
        String userID = context.getUser();
        List<WishlistItem> wishlist = warehouse.getClientWishlist(userID);
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
            lines.add(new OrderLine(w.getProductID(), qty));
        }
        String summary = warehouse.createOrder(userID, lines);
        System.out.println(summary);
    }

    public void exit() 
    {
        System.out.println("Exiting Client State.");
        context.logout();
    }

    @Override
    public void run() {
        boolean running = true;
        while (running) {
            System.out.println("\nClient State Menu:");
            System.out.println("1. View Client Details");
            System.out.println("2. Show Product List");
            System.out.println("3. Show Client Transactions");
            System.out.println("4. Add to Wishlist");
            System.out.println("5. Display Client Wishlist");
            System.out.println("6. Place Order");
            System.out.println("7. Help");
            System.out.println("0. Exit");
            System.out.print("Enter your choice: ");
            int choice = Integer.parseInt(in.nextLine());

            switch (choice) {
                case CLIENT_DETAILS -> getClientDetails();
                case SHOW_PRODUCT_LIST -> showProductList();
                case SHOW_CLIENT_TRANSACTIONS -> showClientTransactions();
                case ADD_TO_WISHLIST -> addItemToWishlist();
                case DISPLAY_CLIENT_WISHLIST -> displayClientWishlist();
                case PLACE_ORDER -> placeOrder();
                case HELP -> help();
                case LOGOUT -> { exit(); running = false; }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}