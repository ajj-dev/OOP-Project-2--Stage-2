import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

public class ClientState extends WarehouseState
{
    private static ClientState instance;
    private final Warehouse warehouse = Warehouse.getInstance();

    private ClientState() { super(); }

    public static ClientState getInstance()
    {
        if (instance == null) instance = new ClientState();
        return instance;
    }

    @Override
    public void run()
    {
        JFrame frame = prepareFrame();
        frame.setTitle("Warehouse - Client " + context.getUser());
        frame.getContentPane().setLayout(new BorderLayout());   // buttons on top, output below

        JPanel buttons = new JPanel(new FlowLayout());
        buttons.add(new DetailsButton());
        buttons.add(new ProductsButton());
        buttons.add(new TransactionsButton());
        buttons.add(new AddWishlistButton());
        buttons.add(new ShowWishlistButton());
        buttons.add(new PlaceOrderButton());
        buttons.add(new LogoutButton());

        output.setEditable(false);
        frame.getContentPane().add(buttons, BorderLayout.NORTH);
        frame.getContentPane().add(new JScrollPane(output), BorderLayout.CENTER);
        showFrame(frame);
    }

    private void showClientDetails()
    {
        Client client = warehouse.getClient(context.getUser());
        if (client == null) { display("Client not found."); return; }
        display("Client ID: " + client.getID()
              + "\nName: "    + client.getName()
              + "\nAddress: " + client.getAddress());
    }

    private void showProductList()
    {
        List<ProductView> views = warehouse.getProductListView(WarehouseContext.CLIENT);
        if (views.isEmpty()) { display("No products available."); return; }
        StringBuilder sb = new StringBuilder("Products:\n");
        for (ProductView pv : views) sb.append(pv.clientString()).append("\n");
        display(sb.toString());
    }

    private void showClientTransactions()
    {
        Client client = warehouse.getClient(context.getUser());
        if (client == null) { display("Client not found."); return; }
        List<Transaction> ts = client.getTransactions();
        if (ts.isEmpty()) { display("No transactions found for this client."); return; }
        StringBuilder sb = new StringBuilder("Transactions:\n");
        for (Transaction t : ts) sb.append(t).append("\n");
        display(sb.toString());
    }

    private void addItemToWishlist() {
        String productID = promptRequired("Enter product ID:");
        if (productID == null) return;                 // cancelled
        Integer qty = promptInt("Enter quantity:");
        if (qty == null) return;                        // cancelled

        boolean ok = warehouse.addToWishlist(context.getUser(), productID, qty);
        display(ok ? "Product added to wishlist." : "Error: client or product not found.");
    }

    private void showWishlist()
    {
        Client client = warehouse.getClient(context.getUser());
        if (client == null) { display("Client not found."); return; }
        List<WishlistItem> wishlist = client.getWishListItems();
        if (wishlist.isEmpty()) { display("Wishlist is empty."); return; }
        StringBuilder sb = new StringBuilder("Wishlist:\n");
        for (WishlistItem item : wishlist) sb.append(item).append("\n");
        display(sb.toString());
    }

    /** Buy everything in the wishlist (per the test sequence), then show the summary. */
    private void placeOrder()
    {
        List<WishlistItem> wishlist = warehouse.getClientWishlist(context.getUser());
        if (wishlist == null) { display("Client not found."); return; }
        if (wishlist.isEmpty()) { display("Wishlist is empty."); return; }

        List<OrderLine> lines = new ArrayList<>();
        for (WishlistItem w : wishlist)
            lines.add(new OrderLine(w.getProductID(), w.getQuantity()));

        display(warehouse.createOrder(context.getUser(), lines));
    }

    private class DetailsButton extends JButton implements ActionListener {
        DetailsButton() { super("Client details"); addActionListener(this); }
        @Override public void actionPerformed(ActionEvent e) { showClientDetails(); }
    }
    private class ProductsButton extends JButton implements ActionListener {
        ProductsButton() { super("Show products"); addActionListener(this); }
        @Override public void actionPerformed(ActionEvent e) { showProductList(); }
    }
    private class TransactionsButton extends JButton implements ActionListener {
        TransactionsButton() { super("Transactions"); addActionListener(this); }
        @Override public void actionPerformed(ActionEvent e) { showClientTransactions(); }
    }
    private class AddWishlistButton extends JButton implements ActionListener {
        AddWishlistButton() { super("Add to wishlist"); addActionListener(this); }
        @Override public void actionPerformed(ActionEvent e) { addItemToWishlist(); }
    }
    private class ShowWishlistButton extends JButton implements ActionListener {
        ShowWishlistButton() { super("Show wishlist"); addActionListener(this); }
        @Override public void actionPerformed(ActionEvent e) { showWishlist(); }
    }
    private class PlaceOrderButton extends JButton implements ActionListener {
        PlaceOrderButton() { super("Place order"); addActionListener(this); }
        @Override public void actionPerformed(ActionEvent e) { placeOrder(); }
    }
    private class LogoutButton extends JButton implements ActionListener {
        LogoutButton() { super("Logout"); addActionListener(this); }
        @Override public void actionPerformed(ActionEvent e) { context.logout(); }
    }
}