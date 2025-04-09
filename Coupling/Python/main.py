
from order_service import OrderService

if __name__ == "__main__":
    order_service = OrderService()
    order_service.place_order("Laptop", 1, notify=True)
