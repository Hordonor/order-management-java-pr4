package ua.edu.dut.order.processing.model;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@ToString
@Builder
public class Electronics implements Product {
    private final String name;
    private final String category;
    private final BigDecimal price;
    private final String brand;
    private final int warrantyMonths;
}
