import asyncio
import uuid
from datetime import datetime


# === Domain Entities ===

class Order:
    def __init__(self, customer_id, product_ids):
        self.id = str(uuid.uuid4())
        self.customer_id = customer_id
        self.product_ids = product_ids


class OrderPlacedEvent:
    def __init__(self, order_id, customer_id):
        self.order_id = order_id
        self.customer_id = customer_id


class OrderHistoryDto:
    def __init__(self, order_id, customer_id, placed_at):
        self.order_id = order_id
        self.customer_id = customer_id
        self.placed_at = placed_at

    def __repr__(self):
        return f"<OrderHistoryDto order_id={self.order_id}, customer_id={self.customer_id}, placed_at={self.placed_at}>"

# === Simulated Databases ===

class ApplicationDbContext:
    def __init__(self):
        self.orders = []

    async def save_changes(self):
        await asyncio.sleep(0.05)  # simulate async DB write


class OrderHistoryDbContext:
    def __init__(self):
        self.order_histories = []

    async def save_changes(self):
        await asyncio.sleep(0.05)  # simulate async DB write

    async def get_by_customer_id(self, customer_id):
        return [h for h in self.order_histories if h.customer_id == customer_id]


# === Event Publisher ===

class EventPublisher:
    def __init__(self, handlers):
        self.handlers = handlers

    async def publish(self, event):
        for handler in self.handlers:
            await handler.handle(event)


# === Command Handler ===

class PlaceOrderCommand:
    def __init__(self, customer_id, product_ids):
        self.customer_id = customer_id
        self.product_ids = product_ids


class PlaceOrderHandler:
    def __init__(self, db_context, publisher):
        self._context = db_context
        self._publisher = publisher

    async def handle(self, request):
        order = Order(request.customer_id, request.product_ids)
        self._context.orders.append(order)

        await self._context.save_changes()
        await self._publisher.publish(OrderPlacedEvent(order.id, request.customer_id))

        return order.id


# === Query Handler ===

class GetOrderHistoryQuery:
    def __init__(self, customer_id):
        self.customer_id = customer_id


class GetOrderHistoryHandler:
    def __init__(self, read_db):
        self._read_db = read_db

    async def handle(self, request):
        return await self._read_db.get_by_customer_id(request.customer_id)


# === Event Handler ===

class OrderPlacedEventHandler:
    def __init__(self, read_db):
        self._read_db = read_db

    async def handle(self, event):
        view = OrderHistoryDto(
            order_id=event.order_id,
            customer_id=event.customer_id,
            placed_at=datetime.utcnow()
        )
        self._read_db.order_histories.append(view)
        await self._read_db.save_changes()


# === Example Runner ===

async def main():
    write_db = ApplicationDbContext()
    read_db = OrderHistoryDbContext()
    event_handler = OrderPlacedEventHandler(read_db)
    publisher = EventPublisher([event_handler])

    # Handle command
    command = PlaceOrderCommand("cust-abc", ["prod-1", "prod-2"])
    place_handler = PlaceOrderHandler(write_db, publisher)
    order_id = await place_handler.handle(command)
    print(f"✅ Order placed with ID: {order_id}")

    # Handle query
    query = GetOrderHistoryQuery("cust-abc")
    query_handler = GetOrderHistoryHandler(read_db)
    history = await query_handler.handle(query)
    print("📜 Order history:")
    for h in history:
        print(h)


if __name__ == "__main__":
    asyncio.run(main())
