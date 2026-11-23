package ua.edu.dut.order.storage.internal;

import ua.edu.dut.order.processing.model.Order;
import ua.edu.dut.order.processing.model.Product;

import java.util.ArrayList;
import java.util.List;

/** Package implementation; its package is intentionally not exported by the storage module. */
public final class InMemoryOrderStore {
    private final List<Order<? extends Product>> orders = new ArrayList<>();

    public synchronized void save(Order<? extends Product> order) {
        orders.add(order);
    }

    public synchronized List<Order<? extends Product>> findAll() {
        return List.copyOf(orders);
    }

    public synchronized List<Order<? extends Product>> findByProductType(Class<? extends Product> productType) {
        return orders.stream()
                .filter(order -> productType.isInstance(order.getProduct()))
                .toList();
    }
}
