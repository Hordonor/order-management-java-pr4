package ua.edu.dut.order.processing.service;

import com.github.javafaker.Faker;
import ua.edu.dut.order.processing.model.Clothing;
import ua.edu.dut.order.processing.model.Electronics;
import ua.edu.dut.order.processing.model.Product;

import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;

public final class ProductFactory {
    private final Faker faker;

    public ProductFactory() {
        this(new Faker(Locale.US));
    }

    public ProductFactory(Faker faker) {
        this.faker = faker;
    }

    public List<Product> createSampleProducts() {
        Electronics headphones = Electronics.builder()
                .name(faker.commerce().productName())
                .category("Електроніка")
                .price(randomPrice(500, 8_000))
                .brand(faker.company().name())
                .warrantyMonths(faker.number().numberBetween(12, 37))
                .build();

        Electronics smartwatch = Electronics.builder()
                .name(faker.commerce().productName())
                .category("Електроніка")
                .price(randomPrice(1_000, 15_000))
                .brand(faker.company().name())
                .warrantyMonths(faker.number().numberBetween(12, 37))
                .build();

        Clothing jacket = Clothing.builder()
                .name(faker.commerce().productName())
                .category("Одяг")
                .price(randomPrice(800, 6_000))
                .brand(faker.company().name())
                .size(faker.options().option("S", "M", "L", "XL"))
                .material(faker.commerce().material())
                .build();

        Clothing shirt = Clothing.builder()
                .name(faker.commerce().productName())
                .category("Одяг")
                .price(randomPrice(300, 3_000))
                .brand(faker.company().name())
                .size(faker.options().option("S", "M", "L", "XL"))
                .material(faker.commerce().material())
                .build();

        return List.of(headphones, smartwatch, jacket, shirt);
    }

    private BigDecimal randomPrice(int minimum, int maximum) {
        return BigDecimal.valueOf(faker.number().randomDouble(2, minimum, maximum));
    }
}
