
from database import Database
from notification_service import NotificationService
import helper

class OrderService:
    def __init__(self):
        self.db = Database()
        self.notifier = NotificationService()

    def place_order(self, product, quantity, notify):
        customer = self.db.get_customer()
        self.db.insert_order(customer, product, quantity)
        helper.log("Order placed.")

        if notify:
            self.notifier.send_notification(customer)
