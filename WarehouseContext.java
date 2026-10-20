import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;

public class WarehouseContext
{
    // state codes
    public static final int CLIENT = 0, CLERK = 1, MANAGER = 2, LOGIN = 3;

    // transition events
    public static final int TO_CLIENT = 0, TO_CLERK = 1, TO_MANAGER = 2,
                            TO_LOGIN = 3, TERMINATE = 4;

    private static WarehouseContext instance;

    private final WarehouseState[] states = new WarehouseState[4];
    private final int[][] nextState = new int[4][5];

    private int currentState = LOGIN;
    private String userID;           

    private static final class Frame
    {
        final int state; final String userID;
        Frame(int state, String userID) { this.state = state; this.userID = userID; }
    }
    private final Deque<Frame> returnStack = new ArrayDeque<>();

    private WarehouseContext()
    {
        instance = this;

        states[CLIENT]  = ClientState.getInstance();
        states[CLERK]   = ClerkState.getInstance();
        states[MANAGER] = ManagerState.getInstance();
        states[LOGIN]   = LoginState.getInstance();

        for (int[] row : nextState) Arrays.fill(row, -2); 

        nextState[LOGIN][TO_CLIENT]  = CLIENT;
        nextState[LOGIN][TO_CLERK]   = CLERK;
        nextState[LOGIN][TO_MANAGER] = MANAGER;
        nextState[LOGIN][TERMINATE]  = -1;

        nextState[CLIENT][TO_CLERK]  = CLERK;
        nextState[CLIENT][TO_LOGIN]  = LOGIN;

        nextState[CLERK][TO_CLIENT]  = CLIENT;
        nextState[CLERK][TO_MANAGER] = MANAGER;
        nextState[CLERK][TO_LOGIN]   = LOGIN;

        nextState[MANAGER][TO_CLERK] = CLERK;
        nextState[MANAGER][TO_LOGIN] = LOGIN;

        currentState = LOGIN;
    }

    public static WarehouseContext getInstance()
    {
        if (instance == null) instance = new WarehouseContext();
        return instance;
    }

    public void changeState(int transition)
    {
        int next = nextState[currentState][transition];
        if (next == -1) { terminate(); }                       
        else if (next == -2) { System.out.println("Invalid transition."); }
        else { currentState = next; states[currentState].run(); }
    }

    public void login(int role, String id)
    {
        userID = id;
        changeState(roleToEvent(role));
    }

    public void become(int targetRole, String id)
    {
        returnStack.push(new Frame(currentState, userID));
        userID = id;
        changeState(roleToEvent(targetRole));
    }

    public void logout()
    {
        int target; String restoreID;
        if (!returnStack.isEmpty())
        {
            Frame f = returnStack.pop();
            target = f.state; restoreID = f.userID;
        }
        else { target = LOGIN; restoreID = null; }

        userID = restoreID;
        changeState(roleToEvent(target));
    }

    public void terminate()
    {
        System.out.println("Goodbye.");
        System.exit(0);
    }

    public String getUser() { return userID; }
    public int getActingRole() { return currentState; }

    public void process() { states[currentState].run(); }

    private int roleToEvent(int role)
    {
        switch (role)
        {
            case CLIENT:  return TO_CLIENT;
            case CLERK:   return TO_CLERK;
            case MANAGER: return TO_MANAGER;
            case LOGIN:   return TO_LOGIN;
            default:      return TERMINATE;
        }
    }

    public static void main(String[] args)
    {
        WarehouseContext.getInstance().process();
    }
}