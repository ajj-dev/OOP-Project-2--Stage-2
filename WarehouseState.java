import java.util.Scanner;

/**
 * Base class for every menu state in the text FSM.
 *
 * <p>Holds the shared singleton context and a shared input reader, and provides
 * the reusable token/number/yes-no input helpers (re-prompt on bad input rather
 * than crashing). Each concrete state implements {@link #run()} as its own menu
 * loop; run() returns when the user picks a command that leaves the state, at
 * which point the context's driver loop advances to the next state.</p>
 */
public abstract class WarehouseState
{
    protected static final WarehouseContext context = WarehouseContext.getInstance();

    protected static final Scanner in = new Scanner(System.in);

    public abstract void run();

    protected int getCommand(String prompt)
    {
        while (true)
        {
            System.out.print(prompt);
            String line = in.nextLine().trim();
            try { return Integer.parseInt(line); }
            catch (NumberFormatException e) { System.out.println("Please enter a number."); }
        }
    }

    protected String getToken(String prompt)
    {
        System.out.print(prompt);
        return in.nextLine().trim();
    }

    protected int getQuantity(String prompt)
    {
        while (true)
        {
            int n = getCommand(prompt);
            if (n >= 0) return n;
            System.out.println("Quantity cannot be negative.");
        }
    }

    protected boolean yesOrNo(String prompt)
    {
        System.out.print(prompt + " (y/n): ");
        return in.nextLine().trim().equalsIgnoreCase("y");
    }
}