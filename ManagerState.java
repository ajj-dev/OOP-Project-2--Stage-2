import java.util.ArrayList;
import java.util.List;

public class ManagerState extends WarehouseState 
{
    private static ManagerState instance;
    private final Warehouse warehouse = Warehouse.getInstance();

    private static final int LOGOUT = 0;
    private static final int ADD_PRODUCT = 1;
    private static final int DISPLAY_WAITLIST = 2;
    private static final int RECEIVE_SHIPMENT= 3;
    private static final int BECOME_CLERK = 4;
    private static final int HELP = 5;

    private ManagerState() {
        super();
    }

    public static ManagerState getInstance() {
        if (instance == null) {
            instance = new ManagerState();
        }
        return instance;
    }

    @Override
    public void run() {
        boolean active = true;
        while(active)
        {
            int command = getCommand("Manager Menu:\n" +
                    "1. Add Product\n" +
                    "2. Display Waitlist\n" +
                    "3. Receive Shipment\n" +
                    "4. Become Clerk\n" +
                    "5. Help\n" +
                    "0. Logout\n" +
                    "Enter command: ");

            switch (command) {
                case ADD_PRODUCT -> addProduct();
                case DISPLAY_WAITLIST -> displayWaitlist();
                case RECEIVE_SHIPMENT -> receiveShipment();
                case BECOME_CLERK -> { becomeClerk(); active = false; }
                case HELP -> help();
                case LOGOUT -> { logout(); active = false; }
                default -> System.out.println("Invalid command. Please try again.");
            }
        }
    }

    public void help() {
        System.out.println("Manager State Help:");
        System.out.println("1. Add Product");
        System.out.println("2. Display Waitlist");
        System.out.println("3. Receive Shipment");
        System.out.println("4. Become Clerk");
        System.out.println("0. Logout");
    }

    public void addProduct() {
        System.out.print("Enter name: ");  String name = in.nextLine();
        System.out.print("Enter price: "); double price = Double.parseDouble(in.nextLine());
        System.out.print("Enter stock: "); int stock = Integer.parseInt(in.nextLine());
        String id = warehouse.addProduct(name, price, stock);
        System.out.println("Product added: ID=" + id);
    }

    public void displayWaitlist() {
        System.out.print("Enter product ID: ");
        String productID = in.nextLine().trim();
        List<WaitlistView> waitlist = warehouse.getProductWaitlistView(productID);
        if (waitlist == null) { System.out.println("Product not found."); return; }
        if (waitlist.isEmpty()) { System.out.println("Waitlist is empty."); return; }
        for (WaitlistView item : waitlist)
        {
            System.out.printf("Client=%s, Name=%s, Price=$%.2f, WaitQty=%d, Stock=%d%n",
            item.getClientID(), item.getProductName(), item.getPrice(),
            item.getWaitingQuantity(), item.getStock());
        }
    }

    public void receiveShipment() {
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

    public void becomeClerk() {
        context.login(context.CLERK, null);
    }

    public void logout() {
        context.logout();
    }

}