package ua.edu.dut.order.threading;

import ua.edu.dut.order.processing.exception.InvalidOrderException;
import ua.edu.dut.order.processing.model.Clothing;
import ua.edu.dut.order.processing.model.Electronics;
import ua.edu.dut.order.processing.model.Order;
import ua.edu.dut.order.processing.model.Product;
import ua.edu.dut.order.processing.service.OrderProcessor;
import ua.edu.dut.order.processing.service.ProductFactory;
import ua.edu.dut.order.storage.api.OrderRepository;
import ua.edu.dut.order.threading.api.OrderThreadManager;

import java.io.PrintStream;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class Main {
    private Main() {
    }

    public static void main(String[] args) {
        System.setOut(new PrintStream(System.out, true, StandardCharsets.UTF_8));
        System.setErr(new PrintStream(System.err, true, StandardCharsets.UTF_8));
        ProductFactory productFactory = new ProductFactory();
        List<Product> products = productFactory.createSampleProducts();
        List<OrderProcessor<? extends Product>> processors = products.stream()
                .<OrderProcessor<? extends Product>>map(OrderProcessor::new)
                .toList();

        OrderRepository repository = new OrderRepository();
        try (OrderThreadManager threadManager = new OrderThreadManager(repository)) {
            List<Order<? extends Product>> orders = threadManager.processAll(processors);

            System.out.println("Оброблені замовлення:");
            orders.forEach(System.out::println);

            List<Order<? extends Product>> electronicsOrders = repository.findByProductType(Electronics.class);
            List<Order<? extends Product>> clothingOrders = repository.findByProductType(Clothing.class);
            System.out.printf("\nЗбережено замовлень: %d (електроніка: %d, одяг: %d)%n",
                    repository.findAll().size(), electronicsOrders.size(), clothingOrders.size());

            List<Order<? extends Product>> expensiveOrders = orders.stream()
                    .filter(order -> order.getTotalPrice().compareTo(new BigDecimal("5000")) >= 0)
                    .toList();
            System.out.println("\nЗамовлення від 5000 грн:");
            expensiveOrders.stream()
                    .map(Order::getProduct)
                    .map(Product::getName)
                    .forEach(System.out::println);
        } catch (InvalidOrderException exception) {
            System.err.println("Не вдалося обробити замовлення: " + exception.getMessage());
        }

        demonstrateInvalidOrder();
    }

    private static void demonstrateInvalidOrder() {
        Electronics invalidProduct = Electronics.builder()
                .name("Товар із некоректною ціною")
                .category("Електроніка")
                .price(BigDecimal.ZERO)
                .brand("Demo")
                .warrantyMonths(12)
                .build();
        try {
            new OrderProcessor<>(invalidProduct).process();
        } catch (InvalidOrderException exception) {
            System.out.println("\nПеревірка винятку: " + exception.getMessage());
        }
    }
}
