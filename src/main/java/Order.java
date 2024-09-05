import lombok.With;

import java.util.List;

@With
public record Order(
        String id,
        List<Product> products,
        OrderStatus status
) {
    public Order(String id, List<Product> products) {
        this(id, products, OrderStatus.PROCESSING);
    }
}
