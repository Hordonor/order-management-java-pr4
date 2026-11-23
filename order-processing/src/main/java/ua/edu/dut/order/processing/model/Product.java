package ua.edu.dut.order.processing.model;

import java.math.BigDecimal;

public interface Product {
    String getName();

    String getCategory();

    BigDecimal getPrice();
}
