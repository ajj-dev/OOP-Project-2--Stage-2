import java.util.ArrayList;
import java.util.List;

/**
 * Manages the collection of {Product} objects in the warehouse system.
 *
 * Provides operations for adding products and searching for products by
 * their unique product ID. Products are guaranteed to have  
 * unique IDs within
 * a single catalog instance.
 *
 * @author Aj Johnson, Aaditya Malla. Chidera Izuora
 * @version 1.0
 */
public class Catalog 
{
    private final List<Product> products = new ArrayList<>();
    /**
     * Adds a product to the catalog if its ID is not already present.
     *
     * @param product the product to add; must not be {@code null}
     * @return {@code true} if the product was added successfully,
     *         {@code false} if a product with the same ID already exists
     */
    public boolean addProduct(Product product) 
    {
        if (searchForProduct(product.getID()) != null) 
            return false;
        products.add(product);
        return true;
    }
     /**
     * Searches for a product by its unique product ID.
     *
     * @param productID the ID of the product to search for
     * @return the matching {@link Product}, or {@code null} if no product
     *         with the given ID exists in the catalog
     */
    public Product searchForProduct(String productID) 
    {
        for (Product p : products) 
        {
            if (p.getID().equals(productID)) 
                return p;
        }
        return null;
    }
     /**
     * Returns a copy of all products currently in the catalog.
     *
     *
     * @return a new {@link List} containing all products in the catalog
     */
    // Temporary copy
    public List<Product> getAllProducts() { return new ArrayList<>(products); }
}