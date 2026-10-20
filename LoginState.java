public class LoginState extends WarehouseState 
{
    private static LoginState instance;
    private final Warehouse warehouse = Warehouse.getInstance();

    private static final int QUIT = 0;
    private static final int LOGIN_CLIENT = 1;
    private static final int LOGIN_CLERK = 2;
    private static final int LOGIN_MANAGER = 3;
    private static final int HELP = 4;

    private LoginState() {
        super();
    }

    public static LoginState getInstance() {
        if (instance == null) {
            instance = new LoginState();
        }
        return instance;
    }

    @Override
    public void run() {
        boolean active = true;
        while(active)
        {
            int command = getCommand("Login Menu:\n" +
                    "1. Login as Client\n" +
                    "2. Login as Clerk\n" +
                    "3. Login as Manager\n" +
                    "4. Help\n" +
                    "0. Quit\n" +
                    "Enter command: ");

            switch (command) {
                case LOGIN_CLIENT -> { if (loginClient()) active = false; }
                case LOGIN_CLERK -> { context.login(WarehouseContext.CLERK, null); active = false; }
                case LOGIN_MANAGER -> { context.login(WarehouseContext.MANAGER, null); active = false; }
                case HELP -> help();
                case QUIT -> { context.terminate(); active = false; }
                default -> System.out.println("Invalid command. Please try again.");
            }
        }
    }

    private boolean loginClient(){
        String userID = getToken("Enter Client ID: ");
        if (warehouse.getClient(userID) == null) {
            System.out.println("Client ID not found.");
            return false;
        }
        context.login(context.CLIENT, userID);
        return true;
    }

    public void help() {
        System.out.println("Login State Help:");
        System.out.println("1. Login as Client");
        System.out.println("2. Login as Clerk");
        System.out.println("3. Login as Manager");
        System.out.println("0. Quit");
    }
}
