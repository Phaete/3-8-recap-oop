import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class ShopService {
    private final ProductRepo productRepo;
    private final OrderRepo orderRepo;
    private final IdService idService;

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

    /**
     * Updates an existing order with a new status.
     *
     * @param orderId   the ID of the order to be updated
     * @param newStatus the new status of the order
     */
    public void updateOrder(String orderId, OrderStatus newStatus) {
        // create the new order
        Order newOrder = orderRepo.getOrderById(orderId).withStatus(newStatus);
        // remove the old order from the list or map first due to the map simply overwriting the old order, but list does not
        orderRepo.removeOrder(orderId);
        // add the new order
        orderRepo.addOrder(newOrder);
    }
}
