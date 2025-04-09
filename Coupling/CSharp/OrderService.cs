
using System;

namespace CouplingRefactorExercise
{
    public class OrderService
    {
        private Database db = new Database();
        private NotificationService notifier = new NotificationService();

        public void PlaceOrder(string product, int quantity, bool notify)
        {
            var customer = db.GetCustomer();
            db.InsertOrder(customer, product, quantity);
            Helper.Log("Order placed.");

            if (notify)
            {
                notifier.SendNotification(customer);
            }
        }
    }
}
