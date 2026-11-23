package ua.edu.dut.order.storage.api;

import ua.edu.dut.order.processing.model.Order;
import ua.edu.dut.order.processing.model.Product;
import ua.edu.dut.order.storage.internal.InMemoryOrderStore;

import java.util.List;
import java.util.Objects;

public final class OrderRepository {
    private final InMemoryOrderStore store = new InMemoryOrderStore();

    public void save(Order<? extends Product> order) {
        store.save(Objects.requireNonNull(order, "Замовлення не може бути null."));
    }

    public List<Order<? extends Product>> findAll() {
        return store.findAll();
    }

    public List<Order<? extends Product>> findByProductType(Class<? extends Product> productType) {
        Objects.requireNonNull(productType, "Тип товару не може бути null.");
        return store.findByProductType(productType);
    }
}
