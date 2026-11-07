import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

public class ManagerState extends WarehouseState 
{
    private static ManagerState instance;
    private final Warehouse warehouse = Warehouse.getInstance();

    private ManagerState() {
        super();
    }

    public static ManagerState getInstance() {
        if (instance == null) {
            instance = new ManagerState();
        }
        return instance;
    }

    public void addProduct() {
        String name = promptRequired("Enter product name:");
        Double price = promptDouble("Enter product price:");
        Integer stock = promptInt("Enter product stock quantity:");
        String id = warehouse.addProduct(name, price, stock);
        StringBuilder sb = new StringBuilder();
        sb.append("Product added:\nID: ").append(id);
        display(sb.toString());
    }

    public void displayWaitlist() {
        String productID = promptRequired("Enter product ID:");
        List<WaitlistView> waitlist = warehouse.getProductWaitlistView(productID);
        if (waitlist == null) { display("Product not found."); return; }
        if (waitlist.isEmpty()) { display("Waitlist is empty."); return; }
        StringBuilder sb = new StringBuilder("Waitlist for product ID ").append(productID).append(":\n");
        for (WaitlistView item : waitlist)
        {
            sb.append(String.format("Client=%s, Name=%s, Price=$%.2f, WaitQty=%d, Stock=%d%n",
                item.getClientID(), item.getProductName(), item.getPrice(),
                item.getWaitingQuantity(), item.getStock()));
        }
        display(sb.toString());
    }

    public void receiveShipment() {
        List<ShipmentLine> lines = new ArrayList<>();

        display("Enter shipment lines (product ID and quantity). Leave product ID blank to finish.");
        while (true)
        {
            String productID = promptRequired("Enter product ID:");
            if (productID.isEmpty()) break;

            Integer qty = promptInt("Quantity received:");
            if (qty == null) break;                      
            if (qty <= 0) {
                display("Quantity must be positive.");
                continue;
            }
            lines.add(new ShipmentLine(productID, qty));
        }

        if (lines.isEmpty())
        {
            display("No shipment lines entered.");
            return;
        }

        String report = warehouse.receiveShipment(lines);
        display(report);
    }

    public void becomeClerk() {
        context.login(context.CLERK, null);
    }

    public void exit() {
        context.logout();
        display("Exiting Manager State.");
    }

    @Override
    public void run() {
        JFrame frame = prepareFrame();
        frame.setTitle("Warehouse - Clerk");
        frame.getContentPane().setLayout(new BorderLayout());   // buttons on top, output below

        JPanel buttons = new JPanel(new FlowLayout());
        buttons.add(new AddProductButton());
        buttons.add(new DisplayWaitlistButton());
        buttons.add(new ReceiveShipmentButton());
        buttons.add(new BecomeClerkButton());
        buttons.add(new LogoutButton());

        output.setEditable(false);
        frame.getContentPane().add(buttons, BorderLayout.NORTH);
        frame.getContentPane().add(new JScrollPane(output), BorderLayout.CENTER);
        showFrame(frame);
    }

    private class AddProductButton extends JButton implements ActionListener {
        AddProductButton() { super("Add Product"); addActionListener(this); }
        @Override public void actionPerformed(ActionEvent e) { addProduct(); }
    }

    private class DisplayWaitlistButton extends JButton implements ActionListener {
        DisplayWaitlistButton() { super("Display Waitlist"); addActionListener(this); }
        @Override public void actionPerformed(ActionEvent e) { displayWaitlist(); }
    }

    private class ReceiveShipmentButton extends JButton implements ActionListener {
        ReceiveShipmentButton() { super("Receive Shipment"); addActionListener(this); }
        @Override public void actionPerformed(ActionEvent e) { receiveShipment(); }
    }

    private class BecomeClerkButton extends JButton implements ActionListener {
        BecomeClerkButton() { super("Become Clerk"); addActionListener(this); }
        @Override public void actionPerformed(ActionEvent e) { becomeClerk(); }
    }

    private class LogoutButton extends JButton implements ActionListener {
        LogoutButton() { super("Logout"); addActionListener(this); }
        @Override public void actionPerformed(ActionEvent e) { exit(); }
    }
}