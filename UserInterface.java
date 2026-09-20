import java.util.Scanner;

public class UserInterface 
{
    private final Warehouse warehouse = new Warehouse();
    private final Scanner in = new Scanner(System.in);

    public void run() 
    {
        while (true) 
        {
            System.out.println("\n1) Add client  2) Add product  3) Add to wishlist  "
                    + "4) Print clients  5) Print products  6) Print wishlist  0) Exit");
            System.out.print("Choose: ");
            String choice = in.nextLine().trim();
            switch (choice) 
            {
                case "1": addClient(); break;
                case "2": addProduct(); break;
                case "3": addToWishlist(); break;
                case "4": warehouse.printAllClients(); break;
                case "5": warehouse.printAllProducts(); break;
                case "6": printClientWishlist(); break;
                case "0": return;
                default:  System.out.println("Invalid option.");
            }
        }
    }

    public void addClient() 
    {
        System.out.print("Enter name: ");    String name = in.nextLine();
        System.out.print("Enter address: "); String address = in.nextLine();
        String id = warehouse.addClient(name, address);
        System.out.println("Client added: ID=" + id + ", Name=" + name + ", Address=" + address);
    }

    public void addProduct() 
    {
        System.out.print("Enter name: ");  String name = in.nextLine();
        System.out.print("Enter price: "); double price = Double.parseDouble(in.nextLine());
        System.out.print("Enter stock: "); int stock = Integer.parseInt(in.nextLine());
        String id = warehouse.addProduct(name, price, stock);
        System.out.println("Product added: ID=" + id);
    }

    public void addToWishlist() 
    {
        System.out.print("Enter client ID: ");  String clientID = in.nextLine();
        System.out.print("Enter product ID: "); String productID = in.nextLine();
        System.out.print("Enter quantity: ");   int qty = Integer.parseInt(in.nextLine());
        boolean ok = warehouse.addToWishlist(clientID, productID, qty);
        System.out.println(ok ? "Product added to wishlist." : "Error: client or product not found.");
    }

    public void printClientWishlist() 
    {
        System.out.print("Enter client ID: "); String clientID = in.nextLine();
        warehouse.printClientWishlist(clientID);
    }

    public static void main(String[] args) 
    {
        new UserInterface().run();
    }
}