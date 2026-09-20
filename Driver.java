public class Driver 
{
    public static void main(String[] args) 
    {
        Warehouse w = new Warehouse();

        String c1 = w.addClient("Ada Lovelace", "1 Analytical Way");
        String c2 = w.addClient("Alan Turing", "2 Enigma Rd");
        String p1 = w.addProduct("Widget", 9.99, 100);
        String p2 = w.addProduct("Gadget", 19.50, 5);

        System.out.println("Created clients: " + c1 + ", " + c2);
        System.out.println("Created products: " + p1 + ", " + p2);

        System.out.println("\nadd valid wishlist entry:   " + w.addToWishlist(c1, p1, 3));
        System.out.println("add same product again:     " + w.addToWishlist(c1, p1, 2)); // should bump qty to 5
        System.out.println("add bad client:             " + w.addToWishlist("C0", p1));
        System.out.println("add bad product:            " + w.addToWishlist(c1, "P0"));

        System.out.println("\n--- All clients ---");   w.printAllClients();
        System.out.println("\n--- All products ---");  w.printAllProducts();
        System.out.println("\n--- " + c1 + " wishlist ---"); w.printClientWishlist(c1);
        System.out.println("\n--- " + c2 + " wishlist (empty) ---"); w.printClientWishlist(c2);
    }
}