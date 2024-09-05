import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class ShopService {
    private ProductRepo productRepo = new ProductRepo();
    private OrderRepo orderRepo = new OrderMapRepo();

    public Order addOrder(List<String> productIds) throws ProductDoesNotExistException {
        List<Product> products = new ArrayList<>();
        for (String productId : productIds) {
            if(productRepo.getProductById(productId).isEmpty()) {
                throw new ProductDoesNotExistException("Product mit der Id: " + productId + " existiert nicht und konnte nicht bestellt werden!");
            }
            Product productToOrder = productRepo.getProductById(productId).get();
            products.add(productToOrder);
        }

        Order newOrder = new Order(UUID.randomUUID().toString(), products);

        return orderRepo.addOrder(newOrder);
    }

    public List<Order> getOrderByStatus(OrderStatus status) {
        return orderRepo.getOrders().stream()
                .filter(order -> order.status() == status)
                .toList();
    }
}
