package ua.edu.dut.order.processing.model;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@ToString
@Builder
public class Order<T extends Product> {
    private final String id;
    private final T product;
    private final int quantity;
    private final BigDecimal totalPrice;
    private final Instant createdAt;
}
