
from database import Database
from customer import Customer

class CustomerService:
    def __init__(self):
        self.db = Database()

    def register_customer(self, name, email):
        customer = Customer(name, email)
        self.db.insert_customer(customer)
