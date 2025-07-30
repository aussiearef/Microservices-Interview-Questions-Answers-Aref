
using System;

namespace CouplingRefactorExercise
{
    public class CustomerService
    {
        // This is type use coupling and can be improved by using an interface
        private Database db = new Database();

        public void RegisterCustomer(string name, string email)
        {
            var customer = new Customer { Name = name, Email = email };
            db.InsertCustomer(customer);
        }
    }
}
