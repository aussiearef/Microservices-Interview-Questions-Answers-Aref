
using System;

namespace CouplingRefactorExercise
{
    public class CustomerService
    {
        private Database db = new Database();

        public void RegisterCustomer(string name, string email)
        {
            var customer = new Customer { Name = name, Email = email };
            db.InsertCustomer(customer);
        }
    }
}
