import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        // Create concrete instances of the OrderRepo and ShopRepo
        OrderRepo orderRepo = new OrderMapRepo();
        ProductRepo productRepo = new ProductRepo();

        // Bonus: ID Generation - Create a concrete implemementation of IdService in main and pass it to the ShopService constructor
        IdService idService = new IdService();

        // Create Product to add to the productRepo
        Product banana = new Product("2", "banana");
        productRepo.addProduct(banana);

        Product peach = new Product("3", "peach");
        productRepo.addProduct(peach);

        Product pear = new Product("4", "pear");
        productRepo.addProduct(pear);

        Product orange = new Product("5", "orange");
        productRepo.addProduct(orange);

        Product cherry = new Product("6", "cherry");
        productRepo.addProduct(cherry);

        // Create an instance of the ShopService
        ShopService shopservice = new ShopService(productRepo, orderRepo, idService);

        // Define three (3) concrete orders and add them all to the ShopService
        try {
            shopservice.addOrder(List.of("1", "4")); // Apple and pear
        } catch (ProductDoesNotExistException e) {
            System.out.println("Could not add the order");
        }
        try {
            shopservice.addOrder(List.of("6", "2")); // Cherry and banana
        } catch (ProductDoesNotExistException e) {
            System.out.println("Could not add the order");
        }
        try {
            shopservice.addOrder(List.of("1", "2", "3", "4", "5")); // Apple, banana, peach, pear and oranges -> vit C bomb
        } catch (ProductDoesNotExistException e) {
            System.out.println("Could not add the order");
        }
    }
}
