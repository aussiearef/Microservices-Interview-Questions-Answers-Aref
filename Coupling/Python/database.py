
from customer import Customer

class Database:
    def insert_customer(self, customer):
        print("Customer inserted into shared DB.")

    def insert_order(self, customer, product, quantity):
        print("Order inserted into shared DB.")

    def get_customer(self):
        return Customer("John Doe", "john@example.com")
