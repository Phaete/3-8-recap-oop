import lombok.RequiredArgsConstructor;

import java.util.*;
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

    /**
     * Retrieves the oldest order for each order status.
     *
     * This function iterates over all orders and determines the oldest order for each status.
     * If no orders exist for a particular status, the function sets the order object equal to null for that status.
     *
     * @return A map containing the oldest order for each order status
     */
    public Map<OrderStatus, Order> getOldestOrderPerStatus() {
        // Create new map for the oldest order per status
        Map<OrderStatus, Order> oldestOrderPerStatus = new HashMap<>();

        // Check if there are any orders that have the order status "PROCESSING"
        if (orderRepo.getOrders().stream().anyMatch(order -> order.status() == OrderStatus.PROCESSING)) {
            oldestOrderPerStatus.put(OrderStatus.PROCESSING, orderRepo.getOrders().stream()
                    .filter(order -> order.status() == OrderStatus.PROCESSING)
                    .min(Comparator.comparing(Order::orderTimeStamp))
                    .get());
        } else {
            // If there are no such orders, set the oldest order per status to null
            oldestOrderPerStatus.put(OrderStatus.PROCESSING, null);
        }

        // Check if there are any orders that have the order status "IN_DELIVERY"
        if (orderRepo.getOrders().stream().anyMatch(order -> order.status() == OrderStatus.IN_DELIVERY)) {
            oldestOrderPerStatus.put(OrderStatus.IN_DELIVERY, orderRepo.getOrders().stream()
                    .filter(order -> order.status() == OrderStatus.IN_DELIVERY)
                    .min(Comparator.comparing(Order::orderTimeStamp))
                    .get());
        } else {
            // If there are no such orders, set the oldest order per status to null
            oldestOrderPerStatus.put(OrderStatus.IN_DELIVERY, null);
        }

        // Check if there are any orders that have the order status "COMPLETED"
        if (orderRepo.getOrders().stream().anyMatch(order -> order.status() == OrderStatus.COMPLETED)) {
            oldestOrderPerStatus.put(OrderStatus.COMPLETED, orderRepo.getOrders().stream()
                    .filter(order -> order.status() == OrderStatus.COMPLETED)
                    .min(Comparator.comparing(Order::orderTimeStamp))
                    .get());
        } else {
            // If there are no such orders, set the oldest order per status to null
            oldestOrderPerStatus.put(OrderStatus.COMPLETED, null);
        }

        // Return the oldest order per status
        return oldestOrderPerStatus;
    }
}
