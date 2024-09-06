import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ShopServiceTest {

    @Test
    void addOrderTest() {
        //GIVEN
        ShopService shopService = new ShopService(new ProductRepo(), new OrderMapRepo(), new IdService());
        List<String> productsIds = List.of("1");

        //WHEN
        Order actual = null;
        try {
            actual = shopService.addOrder(productsIds);
        } catch (ProductDoesNotExistException e) {
            fail("Products with the ids: " + productsIds + " do not exist");
        }

        //THEN
        Order expected = new Order("-1", List.of(new Product("1", "Apfel")));
        assertEquals(expected.products(), actual.products());
        assertNotNull(expected.id());
    }

    @Test
    void addOrderTest_whenInvalidProductId_expectNull() {
        //GIVEN
        ShopService shopService = new ShopService(new ProductRepo(), new OrderMapRepo(), new IdService());
        List<String> productsIds = List.of("1", "2");
        assertThrows(ProductDoesNotExistException.class, () -> {
            shopService.addOrder(productsIds);
        });
    }

    @Test
    void getOrderByStatusTest_whenNoOrders_expectNull() {
        ShopService shopService = new ShopService(new ProductRepo(), new OrderMapRepo(), new IdService());
        assertTrue(shopService.getOrderByStatus(OrderStatus.PROCESSING).isEmpty());
    }

    @Test
    void getOrderByStatusTest_whenOrdersExist_expectList() {
        ProductRepo productRepo = new ProductRepo();
        productRepo.addProduct(new Product("2", "Pear"));
        ShopService shopService = new ShopService(productRepo, new OrderMapRepo(), new IdService());

        try {
            shopService.addOrder(List.of("1", "2"));
        } catch (ProductDoesNotExistException e) {
            fail("Products with the ids: " + List.of("1", "2") + " do not exist");
        }
        assertFalse(shopService.getOrderByStatus(OrderStatus.PROCESSING).isEmpty());
    }

    @Test
    void updateOrderTest_whenOrderDoesNotExist_expectException() {
        ProductRepo productRepo = new ProductRepo();
        productRepo.addProduct(new Product("2", "Pear"));
        ShopService shopService = new ShopService(productRepo, new OrderMapRepo(), new IdService());
        assertThrows(OrderDoesNotExistException.class, () -> {
            shopService.updateOrder("1", OrderStatus.IN_DELIVERY);
        });
    }

    @Test
    void updateOrderTest_whenOrderExists_expectUpdate() {
        ProductRepo productRepo = new ProductRepo();
        productRepo.addProduct(new Product("2", "Pear"));
        ShopService shopService = new ShopService(productRepo, new OrderMapRepo(), new IdService());
        try {
            shopService.addOrder(List.of("1", "2"));
        } catch (ProductDoesNotExistException e) {
            fail("Products with the ids: " + List.of("1", "2") + " do not exist");
        }
        Order order = shopService.getOrderByStatus(OrderStatus.PROCESSING).get(0);
        assertEquals(OrderStatus.PROCESSING, order.status());
        try {
            shopService.updateOrder(order.id(), OrderStatus.IN_DELIVERY);
        } catch (OrderDoesNotExistException e) {
            fail("Order mit der Id: " + order.id() + " existiert nicht und konnte nicht aktualisiert werden!");
        }
        assertEquals(order.id(), shopService.getOrderByStatus(OrderStatus.IN_DELIVERY).get(0).id());
    }
}
