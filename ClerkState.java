import java.util.List;

public class ClerkState extends WarehouseState 
{
    private static ClerkState instance;
    private final Warehouse warehouse = Warehouse.getInstance();

    private static final int LOGOUT = 0;
    private static final int ADD_CLIENT = 1;
    private static final int VIEW_PRODUCT_LIST = 2;
    private static final int VIEW_CLIENT_LIST = 3;
    private static final int VIEW_OUTSTANDING = 4;
    private static final int RECORD_PAYMENT = 5;
    private static final int BECOME_CLIENT = 6;
    private static final int HELP = 7;

    private ClerkState() {
        super();
    }

    public static ClerkState getInstance() {
        if (instance == null) {
            instance = new ClerkState();
        }
        return instance;
    }

    @Override
    public void run() {
        boolean active = true;
        while(active)
        {
            int command = getCommand("Clerk Menu:\n" +
                    "1. Add Client\n" +
                    "2. View Product List\n" +
                    "3. View Client List\n" +
                    "4. View Outstanding Balance Clients\n" +
                    "5. Record Client Payment\n" +
                    "6. Become Client\n" +
                    "7. Help\n" +
                    "0. Logout\n" +
                    "Enter command: ");

            switch (command) {
                case ADD_CLIENT -> addClient();
                case VIEW_PRODUCT_LIST -> viewProductList();
                case VIEW_CLIENT_LIST -> viewClientList();
                case VIEW_OUTSTANDING -> viewOutstandingBalanceClients();
                case RECORD_PAYMENT -> recordClientPayment();
                case BECOME_CLIENT -> { if (becomeClient()) active = false; }
                case HELP -> help();
                case LOGOUT -> { exit(); active = false; }
                default -> System.out.println("Invalid command. Please try again.");

            }
        }
    }

    private boolean becomeClient() {
        String clientID = getToken("Enter Client ID: ");
        if (warehouse.getClient(clientID) == null){
            System.out.println("Client not found.");
            return false;
        }
        context.become(WarehouseContext.CLIENT, clientID);
        return true;
    }
    

    public void help() {
        System.out.println("Clerk State Help:");
        System.out.println("1. Add Client");
        System.out.println("2. View Product List");
        System.out.println("3. View Client List");
        System.out.println("4. View Outstanding Balance Clients");
        System.out.println("5. Record Client Payment");
        System.out.println("6. Become Client");
        System.out.println("0. Exit");
    }

    public void exit() {
        System.out.println("Exiting Clerk State.");
        context.logout();
    }

    public void addClient(){
        System.out.print("Enter name: ");    String name = in.nextLine();
        System.out.print("Enter address: "); String address = in.nextLine();
        String id = warehouse.addClient(name, address);
        System.out.println("Client added: ID=" + id + ", Name=" + name + ", Address=" + address);
    }

    public void viewProductList() {
        List<ProductView> productViews = warehouse.getProductListView(1);
        for (ProductView pv : productViews) {
            System.out.println(pv.clerkString());
        }
    }

    public void viewClientList() {
        List<Client> all = warehouse.getAllClients();
        if (all.isEmpty()) { System.out.println("No clients available."); return; }
        for (Client c : all)
            System.out.printf("ID=%s, Name=%s, Address=%s, Balance=$%.2f%n",
                    c.getID(), c.getName(), c.getAddress(), c.getBalance()); 
        }

    public void viewOutstandingBalanceClients() 
    {
        List<Client> all = warehouse.getAllClients();
        boolean found = false;
        for (Client c : all) {
            if (c.getBalance() < 0) {
                System.out.printf("ID=%s, Name=%s, Address=%s, Balance=$%.2f%n",
                        c.getID(), c.getName(), c.getAddress(), c.getBalance());
                found = true;
            }
        }
        if (!found) {
            System.out.println("No clients with outstanding balances.");
        }
    }

    public void recordClientPayment() {
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