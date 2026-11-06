import java.awt.event.*;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import javax.swing.*;

public class WarehouseContext
{
    // state codes (also the acting role)
    public static final int CLIENT = 0, CLERK = 1, MANAGER = 2, LOGIN = 3;

    // transition events (matrix columns)
    public static final int TO_CLIENT = 0, TO_CLERK = 1, TO_MANAGER = 2,
                            TO_LOGIN = 3, TERMINATE = 4;

    private static WarehouseContext instance;

    private final WarehouseState[] states = new WarehouseState[4];
    private final int[][] nextState = new int[4][5];

    private int currentState = LOGIN;
    private String userID;

    private static JFrame frame;   // the one shared window, reused by every state

    private static final class ReturnFrame
    {
        final int state; final String userID;
        ReturnFrame(int state, String userID) { this.state = state; this.userID = userID; }
    }
    private final Deque<ReturnFrame> returnStack = new ArrayDeque<>();

    private WarehouseContext()
    {
        instance = this;   // break the init cycle: states' getInstance() calls us back

        states[CLIENT]  = ClientState.getInstance();
        states[CLERK]   = ClerkState.getInstance();
        states[MANAGER] = ManagerState.getInstance();
        states[LOGIN]   = LoginState.getInstance();

        for (int[] row : nextState) Arrays.fill(row, -2);   // -2 == invalid by default

        nextState[LOGIN][TO_CLIENT]  = CLIENT;
        nextState[LOGIN][TO_CLERK]   = CLERK;
        nextState[LOGIN][TO_MANAGER] = MANAGER;
        nextState[LOGIN][TERMINATE]  = -1;

        nextState[CLIENT][TO_CLERK]  = CLERK;   // logout back to clerk
        nextState[CLIENT][TO_LOGIN]  = LOGIN;   // logout out to login

        nextState[CLERK][TO_CLIENT]  = CLIENT;  // become client
        nextState[CLERK][TO_MANAGER] = MANAGER; // logout back to manager
        nextState[CLERK][TO_LOGIN]   = LOGIN;   // logout out to login

        nextState[MANAGER][TO_CLERK] = CLERK;   // become clerk
        nextState[MANAGER][TO_LOGIN] = LOGIN;   // logout out to login

        currentState = LOGIN;

        frame = new JFrame("Warehouse");
        frame.addWindowListener(new WindowAdapter()
            { @Override public void windowClosing(WindowEvent e) { System.exit(0); } });
        frame.setSize(520, 400);
        frame.setLocation(320, 200);
    }

    public static WarehouseContext getInstance()
    {
        if (instance == null) instance = new WarehouseContext();
        return instance;
    }

    /** The window every state draws on. */
    public JFrame getFrame() { return frame; }

    public void changeState(int transition)
    {
        int next = nextState[currentState][transition];
        if (next == -1) { terminate(); return; }
        if (next == -2) { JOptionPane.showMessageDialog(frame, "Invalid transition."); return; }
        currentState = next;
        states[currentState].run();   // the new state re-skins the shared frame
    }

    /** Enter a role directly from Login. No frame pushed, so its logout goes to Login. */
    public void login(int role, String id)
    {
        userID = id;
        changeState(roleToEvent(role));
    }

    /** Impersonate: remember the caller, then transition to the target role. */
    public void become(int targetRole, String id)
    {
        returnStack.push(new ReturnFrame(currentState, userID));
        userID = id;
        changeState(roleToEvent(targetRole));
    }

    /** Return to the caller if impersonating, else out to Login. Stack chooses the edge. */
    public void logout()
    {
        int target; String restoreID;
        if (!returnStack.isEmpty())
        {
            ReturnFrame f = returnStack.pop();
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

    public void process() { states[currentState].run(); }

    public static void main(String[] args)
    {
        WarehouseContext.getInstance().process();
    }
}