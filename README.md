# Система багатопотокової обробки замовлень — практична робота 4

Maven-проєкт на Java 17 демонструє узагальнення з обмеженням `T extends Product`, Builder від Lombok, JavaFaker, лямбди, посилання на методи, обробку винятків і JPMS-модулі.

## Запуск

У Windows двічі натисніть `run.bat`. Він перемикає консоль на UTF-8, збирає модулі, запускає багатопотокову демонстрацію та наприкінці чекає на клавішу, перш ніж закрити вікно.

Команди вручну з PowerShell:

```powershell
chcp 65001
./mvnw.cmd clean package
java -Dfile.encoding=UTF-8 --module-path "order-processing/target/order-processing-1.0.0.jar;order-storage/target/order-storage-1.0.0.jar;order-threading/target/order-threading-1.0.0.jar;order-threading/target/dependency" --module order.threading/ua.edu.dut.order.threading.Main
```

`ProductFactory` генерує демонстраційні товари JavaFaker, тому їхні назви, бренди та ціни можуть відрізнятися під час кожного запуску.

## Модулі

- `order.processing` — модель `Product`, `Electronics`, `Clothing`, узагальнені `Order<T>` і `OrderProcessor<T extends Product>`, фабрика JavaFaker та `InvalidOrderException`.
- `order.storage` — публічний `OrderRepository` і приховане сховище. Експортовано лише пакет `api`; пакет `internal` не експортується.
- `order.threading` — `OrderThreadManager` на `ExecutorService`/`CompletableFuture` та демонстраційний `Main`.

## Використані можливості Java

- `Electronics.builder()` і `Clothing.builder()` використовують Lombok `@Builder`.
- `OrderProcessor<T extends Product>` і `Order<T extends Product>` обмежують допустимі типи товаром або його підтипом.
- Валідація товару, ціни та кількості кидає `InvalidOrderException`.
- Lambda передає задачі в пул потоків і фільтрує дорогі замовлення.
- Method references використані для конструювання процесорів, збору `CompletableFuture`, збереження та друку замовлень.
- Синхронізоване внутрішнє сховище безпечно приймає результати з кількох потоків.
