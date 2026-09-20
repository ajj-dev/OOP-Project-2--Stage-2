import java.util.ArrayList;
import java.util.List;

public class Catalog 
{
    private final List<Product> products = new ArrayList<>();

    public boolean addProduct(Product product) 
    {
        if (searchForProduct(product.getID()) != null) 
            return false;
        products.add(product);
        return true;
    }

    public Product searchForProduct(String productID) 
    {
        for (Product p : products) 
        {
            if (p.getID().equals(productID)) 
                return p;
        }
        return null;
    }

    // Temporary copy
    public List<Product> getAllProducts() { return new ArrayList<>(products); }
}