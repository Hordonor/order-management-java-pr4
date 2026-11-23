package ua.edu.dut.order.processing.service;

import ua.edu.dut.order.processing.exception.InvalidOrderException;
import ua.edu.dut.order.processing.model.Order;
import ua.edu.dut.order.processing.model.Product;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public final class OrderProcessor<T extends Product> {
    private final T product;
    private final int quantity;

    public OrderProcessor(T product) {
        this(product, 1);
    }

    public OrderProcessor(T product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public Order<T> process() throws InvalidOrderException {
        if (product == null) {
            throw new InvalidOrderException("Замовлення не містить товару.");
        }
        if (quantity <= 0) {
            throw new InvalidOrderException("Кількість товару має бути більшою за нуль.");
        }
        if (product.getPrice() == null || product.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidOrderException("Ціна товару має бути більшою за нуль.");
        }
        if (product.getName() == null || product.getName().isBlank()) {
            throw new InvalidOrderException("Товар повинен мати назву.");
        }

        return Order.<T>builder()
                .id(UUID.randomUUID().toString())
                .product(product)
                .quantity(quantity)
                .totalPrice(product.getPrice().multiply(BigDecimal.valueOf(quantity)))
                .createdAt(Instant.now())
                .build();
    }
}
