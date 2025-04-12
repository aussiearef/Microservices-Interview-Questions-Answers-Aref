import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

// === Domain Entities ===

class Order {
    public final String id;
    public final String customerId;
    public final List<String> productIds;

    public Order(String customerId, List<String> productIds) {
        this.id = UUID.randomUUID().toString();
        this.customerId = customerId;
        this.productIds = productIds;
    }
}

class OrderPlacedEvent {
    public final String orderId;
    public final String customerId;

    public OrderPlacedEvent(String orderId, String customerId) {
        this.orderId = orderId;
        this.customerId = customerId;
    }
}

class OrderHistoryDto {
    public final String orderId;
    public final String customerId;
    public final LocalDateTime placedAt;

    public OrderHistoryDto(String orderId, String customerId, LocalDateTime placedAt) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.placedAt = placedAt;
    }

    @Override
    public String toString() {
        return "OrderHistoryDto{orderId='" + orderId + "', customerId='" + customerId + "', placedAt=" + placedAt + "}";
    }
}

// === Simulated Databases ===

class ApplicationDbContext {
    public final List<Order> orders = new ArrayList<>();

    public CompletableFuture<Void> saveChanges() {
        return CompletableFuture.runAsync(() -> {
            try {
                Thread.sleep(50);
            } catch (InterruptedException ignored) {}
        });
    }
}

class OrderHistoryDbContext {
    public final List<OrderHistoryDto> orderHistories = new ArrayList<>();

    public CompletableFuture<Void> saveChanges() {
        return CompletableFuture.runAsync(() -> {
            try {
                Thread.sleep(50);
            } catch (InterruptedException ignored) {}
        });
    }

    public CompletableFuture<List<OrderHistoryDto>> getByCustomerId(String customerId) {
        return CompletableFuture.supplyAsync(() ->
            orderHistories.stream()
                .filter(o -> o.customerId.equals(customerId))
                .collect(Collectors.toList())
        );
    }
}

// === Event Publisher and Handler ===

interface EventHandler<T> {
    CompletableFuture<Void> handle(T event);
}

class EventPublisher {
    private final List<EventHandler<OrderPlacedEvent>> handlers;

    public EventPublisher(List<EventHandler<OrderPlacedEvent>> handlers) {
        this.handlers = handlers;
    }

    public CompletableFuture<Void> publish(OrderPlacedEvent event) {
        List<CompletableFuture<Void>> futures = new ArrayList<>();
        for (EventHandler<OrderPlacedEvent> handler : handlers) {
            futures.add(handler.handle(event));
        }
        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
    }
}

// === Command and Handler ===

class PlaceOrderCommand {
    public final String customerId;
    public final List<String> productIds;

    public PlaceOrderCommand(String customerId, List<String> productIds) {
        this.customerId = customerId;
        this.productIds = productIds;
    }
}

class PlaceOrderHandler {
    private final ApplicationDbContext dbContext;
    private final EventPublisher publisher;

    public PlaceOrderHandler(ApplicationDbContext dbContext, EventPublisher publisher) {
        this.dbContext = dbContext;
        this.publisher = publisher;
    }

    public CompletableFuture<String> handle(PlaceOrderCommand command) {
        Order order = new Order(command.customerId, command.productIds);
        dbContext.orders.add(order);

        return dbContext.saveChanges()
            .thenCompose(v -> publisher.publish(new OrderPlacedEvent(order.id, order.customerId)))
            .thenApply(v -> order.id);
    }
}

// === Query and Handler ===

class GetOrderHistoryQuery {
    public final String customerId;

    public GetOrderHistoryQuery(String customerId) {
        this.customerId = customerId;
    }
}

class GetOrderHistoryHandler {
    private final OrderHistoryDbContext readDb;

    public GetOrderHistoryHandler(OrderHistoryDbContext readDb) {
        this.readDb = readDb;
    }

    public CompletableFuture<List<OrderHistoryDto>> handle(GetOrderHistoryQuery query) {
        return readDb.getByCustomerId(query.customerId);
    }
}

// === Event Handler ===

class OrderPlacedEventHandler implements EventHandler<OrderPlacedEvent> {
    private final OrderHistoryDbContext readDb;

    public OrderPlacedEventHandler(OrderHistoryDbContext readDb) {
        this.readDb = readDb;
    }

    @Override
    public CompletableFuture<Void> handle(OrderPlacedEvent event) {
        return CompletableFuture.runAsync(() -> {
            OrderHistoryDto dto = new OrderHistoryDto(
                event.orderId,
                event.customerId,
                LocalDateTime.now()
            );
            readDb.orderHistories.add(dto);
            try {
                Thread.sleep(50);
            } catch (InterruptedException ignored) {}
        });
    }
}

// === Main Method ===

public class CqrsExample {
    public static void main(String[] args) throws Exception {
        ApplicationDbContext writeDb = new ApplicationDbContext();
        OrderHistoryDbContext readDb = new OrderHistoryDbContext();

        EventPublisher publisher = new EventPublisher(
            List.of(new OrderPlacedEventHandler(readDb))
        );

        PlaceOrderHandler placeHandler = new PlaceOrderHandler(writeDb, publisher);
        GetOrderHistoryHandler queryHandler = new GetOrderHistoryHandler(readDb);
