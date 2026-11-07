import java.awt.*;
import java.util.Scanner;
import javax.swing.*;

public abstract class WarehouseState
{
    /** The one FSM context, shared by all states. */
    protected static final WarehouseContext context = WarehouseContext.getInstance();

    /** Shared console reader, for states not yet converted to GUI. */
    protected static final Scanner in = new Scanner(System.in);

    protected final JTextArea output = new JTextArea(16, 44);

    /** Builds this state's screen (GUI) or runs its menu loop (text). */
    public abstract void run();

    /** Clears the shared window and returns it, ready for this state to add buttons. */
    protected JFrame prepareFrame()
    {
        clearOutput();
        JFrame frame = context.getFrame();
        frame.getContentPane().removeAll();
        frame.getContentPane().setLayout(new FlowLayout());
        return frame;
    }

    /** Shows the shared window after this state has added its components. */
    protected void showFrame(JFrame frame)
    {
        frame.setVisible(true);
        frame.revalidate();
        frame.repaint();
        frame.toFront();
    }

    protected int getCommand(String prompt)
    {
        while (true)
        {
            System.out.print(prompt);
            try { return Integer.parseInt(in.nextLine().trim()); }
            catch (NumberFormatException e) { System.out.println("Please enter a number."); }
        }
    }

    protected String getToken(String prompt)
    {
        System.out.print(prompt);
        return in.nextLine().trim();
    }

    protected boolean yesOrNo(String prompt)
    {
        System.out.print(prompt + " (y/n): ");
        return in.nextLine().trim().equalsIgnoreCase("y");
    }

    /** Prompt for a required text field: re-asks on blank, returns null if cancelled. */
    protected String promptRequired(String label) {
        while (true) {
            String value = JOptionPane.showInputDialog(context.getFrame(), label);
            if (value == null) return null;                 // user cancelled
            value = value.trim();
            if (!value.isEmpty()) return value;
            JOptionPane.showMessageDialog(context.getFrame(), label + " cannot be empty.");
        }
    }

    /** Prompt for a whole number: re-asks on bad input, returns null if cancelled. */
    protected Integer promptInt(String label) {
        while (true) {
            String value = JOptionPane.showInputDialog(context.getFrame(), label);
            if (value == null) return null;
            try { return Integer.parseInt(value.trim()); }
            catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(context.getFrame(), label + " must be a whole number.");
            }
        }
    }

    protected Double promptDouble(String label) {
        while (true) {
            String value = JOptionPane.showInputDialog(context.getFrame(), label);
            if (value == null) return null;
            try { return Double.parseDouble(value.trim()); }
            catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(context.getFrame(), label + " must be a number.");
            }
        }
    }

    protected void display(String text) { output.setText(text); }

    protected void clearOutput() { output.setText(""); }
}