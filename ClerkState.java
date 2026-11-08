import java.awt.*;
import java.awt.event.*;
import java.util.List;
import javax.swing.*;

/**
 * Screen and actions available to a warehouse clerk.
 *
 * @author Aj Johnson, Aaditya Malla. Chidera Izuora
 * @version 1.0
 */
public class ClerkState extends WarehouseState
{
    private static ClerkState instance;
    private final Warehouse warehouse = Warehouse.getInstance();
    /**
     * Private constructor to enforce singleton pattern.
     */
    private ClerkState() {
        super();
    }

    /**
     * Returns the singleton instance of ClerkState.
     * @return the singleton instance of ClerkState
     */
    public static ClerkState getInstance() {
        if (instance == null) {
            instance = new ClerkState();
        }
        return instance;
    }

    /**
     * Transitions the user to the client state.
     * @param clientID the ID of the client to transition to
     */
    private void becomeClient() {
        String clientID = promptRequired("Enter client ID:");
        if (clientID == null) { display("Become client cancelled."); return; }
        if (warehouse.getClient(clientID) == null) { display("Client not found."); return; }
        context.become(WarehouseContext.CLIENT, clientID);
    }

   /**
    * Prompts the user for client information and adds a new client to the warehouse.
    */
   public void addClient() {
    String name = promptRequired("Enter client name:");
    if (name == null) { display("Add client cancelled."); return; }

    String address = promptRequired("Enter client address:");
    if (address == null) { display("Add client cancelled."); return; }

    String id = warehouse.addClient(name, address);   // warehouse generates the ID
    display("Client added:\nID: " + id + "\nName: " + name + "\nAddress: " + address);
    }

    /**
     * Displays a list of all products available in the warehouse.
     */
    public void viewProductList() {
        List<ProductView> products = warehouse.getProductListView(WarehouseContext.CLERK);
        if (products.isEmpty()) { display("No products available."); return; }
        StringBuilder sb = new StringBuilder("Products:\n");
        for (ProductView pv : products) sb.append(pv.clerkString()).append("\n");
        display(sb.toString());                    // one setText, with the full list
    }

    /**
     * Displays a list of all clients in the warehouse.
     * If there are no clients, a message indicating this is displayed instead.
     */
    public void viewClientList() {
        List<Client> all = warehouse.getAllClients();
        if (all.isEmpty()) { display("No clients available."); return; }
        StringBuilder sb = new StringBuilder("Clients:\n");
        for (Client c : all)
            sb.append(String.format("ID=%s, Name=%s, Address=%s, Balance=$%.2f%n",
                    c.getID(), c.getName(), c.getAddress(), c.getBalance()));
        display(sb.toString());
    }

    /**
     * Displays clients whose account balance is below zero.
     */
    public void viewOutstandingBalanceClients() {
        List<Client> all = warehouse.getAllClients();
        StringBuilder sb = new StringBuilder("Clients with outstanding balance:\n");
        boolean found = false;
        for (Client c : all) {
            if (c.getBalance() < 0) {
                sb.append(String.format("ID=%s, Name=%s, Address=%s, Balance=$%.2f%n",
                        c.getID(), c.getName(), c.getAddress(), c.getBalance()));
                found = true;
            }
        }
        display(found ? sb.toString() : "No clients with outstanding balances.");
    }

    /**
     * Prompts for and records a client payment.
     */
    public void recordClientPayment() {
        String clientID = promptRequired("Enter client ID: ");
        if (clientID == null) {
            display("Payment cancelled.");
            return;
        }

        Double amount = promptDouble("Enter payment amount: ");
        if (amount == null) {
            display("Payment cancelled.");
            return;
        }

        display(warehouse.receivePayment(clientID, amount));
    }

    /**
     * Logs out of the clerk state.
     */
    public void exit() {
        display("Exiting Clerk State.\n");
        context.logout();
    }

    /**
     * Runs the clerk state, displaying the GUI with available actions.
     */
    @Override
    public void run() {
        JFrame frame = prepareFrame();
        frame.setTitle("Warehouse - Clerk");
        frame.getContentPane().setLayout(new BorderLayout());   // buttons on top, output below

        JPanel buttons = new JPanel(new FlowLayout());
        buttons.add(new AddClientButton());
        buttons.add(new ViewProductListButton());
        buttons.add(new ViewClientListButton());
        buttons.add(new ViewOutstandingBalanceClientsButton());
        buttons.add(new RecordClientPaymentButton());
        buttons.add(new BecomeClientButton());
        buttons.add(new LogoutButton());

        output.setEditable(false);
        frame.getContentPane().add(buttons, BorderLayout.NORTH);
        frame.getContentPane().add(new JScrollPane(output), BorderLayout.CENTER);
        showFrame(frame);
    }

    /**
     * Prepares the frame for the clerk state.
     *
     * @return the prepared JFrame
     */
    private class LogoutButton extends JButton implements ActionListener
    {
        LogoutButton() { super("Logout"); addActionListener(this); }
        @Override public void actionPerformed(ActionEvent e) { exit(); }
    }

    /**
     * Button to add a new client.
     */
    private class AddClientButton extends JButton implements ActionListener
    {
        AddClientButton() { super("Add Client"); addActionListener(this); }
        @Override public void actionPerformed(ActionEvent e) { addClient(); }
    }

    /**
     * Button to view the product list.
     */
    private class ViewProductListButton extends JButton implements ActionListener
    {
        ViewProductListButton() { super("View Product List"); addActionListener(this); }
        @Override public void actionPerformed(ActionEvent e) { viewProductList(); }
    }

    /**
     * Button to view the list of all clients.
     */
    private class ViewClientListButton extends JButton implements ActionListener
    {
        ViewClientListButton() { super("View Client List"); addActionListener(this); }
        @Override public void actionPerformed(ActionEvent e) { viewClientList(); }
    }

    /**
     * Button to view clients with outstanding balances.
     */
    private class ViewOutstandingBalanceClientsButton extends JButton implements ActionListener
    {
        ViewOutstandingBalanceClientsButton() { super("View Outstanding Balance Clients"); addActionListener(this); }
        @Override public void actionPerformed(ActionEvent e) { viewOutstandingBalanceClients(); }
    }

    /**
     * Button to record a client payment.
     */
    private class RecordClientPaymentButton extends JButton implements ActionListener
    {
        RecordClientPaymentButton() { super("Record Client Payment"); addActionListener(this); }
        @Override public void actionPerformed(ActionEvent e) { recordClientPayment(); }
    }

    /**
     * Button to transition to the client state.
     */
    private class BecomeClientButton extends JButton implements ActionListener
    {
        BecomeClientButton() { super("Become Client"); addActionListener(this); }
        @Override public void actionPerformed(ActionEvent e) { becomeClient(); }
    }
}
