package ua.edu.dut.order.threading.api;

import ua.edu.dut.order.processing.exception.InvalidOrderException;
import ua.edu.dut.order.processing.model.Order;
import ua.edu.dut.order.processing.model.Product;
import ua.edu.dut.order.processing.service.OrderProcessor;
import ua.edu.dut.order.storage.api.OrderRepository;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public final class OrderThreadManager implements AutoCloseable {
    private final ExecutorService executor;
    private final OrderRepository repository;

    public OrderThreadManager(OrderRepository repository) {
        this.repository = repository;
        int threadCount = Math.min(8, Math.max(2, Runtime.getRuntime().availableProcessors()));
        this.executor = Executors.newFixedThreadPool(threadCount);
    }

    public List<Order<? extends Product>> processAll(
            List<OrderProcessor<? extends Product>> processors) throws InvalidOrderException {
        List<CompletableFuture<Order<? extends Product>>> futures = processors.stream()
                .map(this::submit)
                .toList();
        try {
            return futures.stream()
                    .map(CompletableFuture::join)
                    .toList();
        } catch (CompletionException exception) {
            if (exception.getCause() instanceof InvalidOrderException invalidOrderException) {
                throw invalidOrderException;
            }
            throw exception;
        }
    }

    private CompletableFuture<Order<? extends Product>> submit(
            OrderProcessor<? extends Product> processor) {
        return CompletableFuture.supplyAsync(() -> {
            Order<? extends Product> order = processUnchecked(processor);
            repository.save(order);
            return order;
        }, executor);
    }

    private static Order<? extends Product> processUnchecked(
            OrderProcessor<? extends Product> processor) {
        return processCaptured(processor);
    }

    private static <T extends Product> Order<T> processCaptured(OrderProcessor<T> processor) {
        try {
            Order<T> order = processor.process();
            System.out.printf("Потік %s обробив товар: %s%n",
                    Thread.currentThread().getName(), order.getProduct().getName());
            return order;
        } catch (InvalidOrderException exception) {
            throw new CompletionException(exception);
        }
    }

    @Override
    public void close() {
        executor.shutdown();
        try {
            if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException exception) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
