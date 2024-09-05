import lombok.With;

import java.time.Instant;
import java.util.List;

@With
public record Order(
        String id,
        List<Product> products,
        OrderStatus status,
        java.time.Instant orderTimeStamp
) {
    public Order(String id, List<Product> products) {
        this(id, products, OrderStatus.PROCESSING, Instant.now());
    }
}
