import java.util.ArrayList;
import java.util.List;

// Stage 2 integration driver.
// One shared Warehouse (singleton):
//   setup -> order within stock -> order exceeding stock (waitlist)
//   -> receive shipment (drain waitlist) -> payments (overpay + partial).
// Each step asserts on real state and prints PASS/FAIL.
public class Driver
{
    private static int checks = 0;
    private static int passed = 0;

    public static void main(String[] args)
    {
        Warehouse w = Warehouse.getInstance();

        System.out.println("=== Stage 2 Driver ===");

        // ---------------------------------------------------------------
        // Setup: two clients, two products with known stock.
        // ---------------------------------------------------------------
        String takumi  = w.addClient("Takumi Fujiwara",   "1 Akina Pass");
        String keisuke = w.addClient("Keisuke Takahashi", "2 Akagi Pass");

        String tires     = w.addProduct("Tires",      80.00, 5);    // price 80, stock 5
        String brakePads = w.addProduct("Brake Pads", 20.00, 100);  // price 20, stock 100

        System.out.println("Clients: " + takumi + ", " + keisuke);
        System.out.println("Products: " + tires + " (stock 5), " + brakePads + " (stock 100)\n");

        // ===============================================================
        // 1. Order WITHIN stock.
        //    Takumi wishlists 3 Brake Pads, orders all 3. Stock 100 -> 97,
        //    Takumi debited 3 * 20 = 60 (balance -60), wishlist cleared.
        // ===============================================================
        System.out.println("--- 1. Order within stock ---");
        w.addToWishlist(takumi, brakePads, 3);
        List<OrderLine> order1 = new ArrayList<>();
        order1.add(new OrderLine(brakePads, 3));
        System.out.println(w.createOrder(takumi, order1));

        check("Brake Pads stock 100 -> 97", productStock(w, brakePads) == 97);
        check("Takumi balance == -60.00", approx(clientBalance(w, takumi), -60.00));
        check("Takumi brake-pad wishlist cleared", w.getClientWishlistView(takumi).isEmpty());
        System.out.println();

        // ===============================================================
        // 2. Order EXCEEDING stock -> waitlist the shortfall.
        //    Takumi wants 8 Tires, stock is 5. Fills 5 (debit 5 * 80 = 400),
        //    waitlists 3. Tires stock -> 0. Takumi balance -60 -> -460.
        //    Critically: debit is for the FILLED 5 only, not all 8.
        // ===============================================================
        System.out.println("--- 2. Order exceeding stock ---");
        w.addToWishlist(takumi, tires, 8);
        List<OrderLine> order2 = new ArrayList<>();
        order2.add(new OrderLine(tires, 8));
        System.out.println(w.createOrder(takumi, order2));

        check("Tires stock 5 -> 0", productStock(w, tires) == 0);
        check("Takumi waitlisted 3 Tires", waitlistQtyFor(w, tires, takumi) == 3);
        check("Takumi balance == -460.00 (debited 5, not 8)", approx(clientBalance(w, takumi), -460.00));
        System.out.println();

        // ===============================================================
        // 3. Receive shipment -> drain waitlist FIFO, rest to stock.
        //    10 Tires arrive. Takumi's 3 waitlisted fill first (debit 3 * 80 = 240),
        //    balance -460 -> -700, waitlist empties, remaining 7 -> stock.
        // ===============================================================
        System.out.println("--- 3. Receive shipment ---");
        List<ShipmentLine> ship = new ArrayList<>();
        ship.add(new ShipmentLine(tires, 10));
        System.out.println(w.receiveShipment(ship));

        check("Takumi waitlist drained", waitlistQtyFor(w, tires, takumi) == 0);
        check("Tires stock 0 -> 7 (10 recv - 3 to waitlist)", productStock(w, tires) == 7);
        check("Takumi balance == -700.00 (waitlist fill debited 240)", approx(clientBalance(w, takumi), -700.00));
        System.out.println();

        // ===============================================================
        // 4. DOUBLE-CHARGE CHECK (the headline).
        //    Takumi ordered 8 Tires total, all 8 are now his (5 at order,
        //    3 at shipment) at $80 = $640 for Tires, plus $60 for Brake Pads.
        //    Total owed must be exactly $700 — each unit charged once.
        //    If the waitlisted 3 were charged at BOTH order and shipment,
        //    this would read -940. It must be -700.
        // ===============================================================
        System.out.println("--- 4. Double-charge check ---");
        double expected = -(3 * 20.00 + 8 * 80.00); // -700.00
        check("Takumi charged once per unit (balance == -700.00)",
              approx(clientBalance(w, takumi), expected));
        System.out.println("  Takumi balance: " + String.format("$%.2f", clientBalance(w, takumi))
                + " (expected " + String.format("$%.2f", expected) + ")\n");

        // ===============================================================
        // 5. Payments: overpay -> credit, partial -> reduced debt.
        //    Takumi owes 700. Pay 760 -> +60 credit. Then Keisuke incurs
        //    a 100 debt via an order and pays 40 -> still owes 60.
        // ===============================================================
        System.out.println("--- 5. Payments ---");
        System.out.println(w.receivePayment(takumi, 760.00));          // -700 + 760 = +60
        check("Takumi overpaid -> +60.00 credit", approx(clientBalance(w, takumi), 60.00));

        // Keisuke: incur a debt via an order, then partially pay it.
        w.addToWishlist(keisuke, brakePads, 5);
        List<OrderLine> keisukeOrder = new ArrayList<>();
        keisukeOrder.add(new OrderLine(brakePads, 5));                  // 5 * 20 = 100 debit
        w.createOrder(keisuke, keisukeOrder);                          // Keisuke balance -100
        System.out.println(w.receivePayment(keisuke, 40.00));          // -100 + 40 = -60
        check("Keisuke partial payment -> still owes 60.00", approx(clientBalance(w, keisuke), -60.00));
        System.out.println();

        // ---------------------------------------------------------------
        // Summary
        // ---------------------------------------------------------------
        System.out.println("=== " + passed + "/" + checks + " checks passed ===");
        System.out.println(passed == checks ? "RESULT: PASS" : "RESULT: FAIL");
    }

    // ---- assertion helper ----
    private static void check(String label, boolean condition)
    {
        checks++;
        if (condition) passed++;
        System.out.println((condition ? "  [PASS] " : "  [FAIL] ") + label);
    }

    private static boolean approx(double a, double b)
    {
        return Math.abs(a - b) < 1e-6;
    }

    // ---- state readers (go through the facade's public API only) ----
    private static double clientBalance(Warehouse w, String clientID)
    {
        for (Client c : w.getAllClients())
            if (c.getID().equals(clientID)) return c.getBalance();
        return Double.NaN;
    }

    private static int productStock(Warehouse w, String productID)
    {
        for (Product p : w.getAllProducts())
            if (p.getID().equals(productID)) return p.getStock();
        return -1;
    }

    private static int waitlistQtyFor(Warehouse w, String productID, String clientID)
    {
        List<WaitlistView> wl = w.getProductWaitlistView(productID);
        if (wl == null) return 0;
        int total = 0;
        for (WaitlistView v : wl)
            if (v.getClientID().equals(clientID)) total += v.getWaitingQuantity();
        return total;
    }
}