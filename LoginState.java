import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class LoginState extends WarehouseState
{
    private static LoginState instance;
    private final Warehouse warehouse = Warehouse.getInstance();

    private LoginState() { super(); }

    public static LoginState getInstance()
    {
        if (instance == null) instance = new LoginState();
        return instance;
    }

    /** Build the login screen on the shared window. Event-driven: no loop. */
    @Override
    public void run()
    {
        JFrame frame = prepareFrame();
        frame.setTitle("Warehouse - Login");
        JPanel buttonsPanel = new JPanel(new FlowLayout());

        buttonsPanel.add(new ClientLoginButton());
        buttonsPanel.add(new ClerkLoginButton());
        buttonsPanel.add(new ManagerLoginButton());
        buttonsPanel.add(new QuitButton());

        frame.getContentPane().add(buttonsPanel, BorderLayout.NORTH);

        showFrame(frame);
    }

    private void loginClient()
    {
        String id = JOptionPane.showInputDialog(context.getFrame(), "Enter client ID:");
        if (id == null) return;                 // user cancelled the dialog
        id = id.trim();
        if (warehouse.getClient(id) == null)
        {
            JOptionPane.showMessageDialog(context.getFrame(), "Client not found.");
            return;                             // stay on the login screen
        }
        context.login(WarehouseContext.CLIENT, id);
    }

    private void loginClerk()   { context.login(WarehouseContext.CLERK, null); }
    private void loginManager() { context.login(WarehouseContext.MANAGER, null); }
    private void quit()         { context.changeState(WarehouseContext.TERMINATE); }

    private class ClientLoginButton extends JButton implements ActionListener
    {
        ClientLoginButton() { super("Login as Client"); addActionListener(this); }
        @Override public void actionPerformed(ActionEvent e) { loginClient(); }
    }

    private class ClerkLoginButton extends JButton implements ActionListener
    {
        ClerkLoginButton() { super("Login as Clerk"); addActionListener(this); }
        @Override public void actionPerformed(ActionEvent e) { loginClerk(); }
    }

    private class ManagerLoginButton extends JButton implements ActionListener
    {
        ManagerLoginButton() { super("Login as Manager"); addActionListener(this); }
        @Override public void actionPerformed(ActionEvent e) { loginManager(); }
    }

   
    private class QuitButton extends JButton implements ActionListener
    {
        QuitButton() { super("Quit"); addActionListener(this); }
        @Override public void actionPerformed(ActionEvent e) { quit(); }
    }
}