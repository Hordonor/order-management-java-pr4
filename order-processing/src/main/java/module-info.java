module order.processing {
    requires javafaker;
    requires java.sql;
    requires static lombok;

    exports ua.edu.dut.order.processing.model;
    exports ua.edu.dut.order.processing.exception;
    exports ua.edu.dut.order.processing.service;
}
