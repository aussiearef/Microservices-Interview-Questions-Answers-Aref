
using System;

namespace CouplingRefactorExercise
{
    public class Database
    {
        public void InsertCustomer(Customer customer)
        {
            Console.WriteLine("Customer inserted into shared DB.");
        }

        public void InsertOrder(Customer customer, string product, int quantity)
        {
            Console.WriteLine("Order inserted into shared DB.");
        }

        public Customer GetCustomer()
        {
            return new Customer { Name = "John Doe", Email = "john@example.com" };
        }
    }
}
