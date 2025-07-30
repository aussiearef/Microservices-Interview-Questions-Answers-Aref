
using System;

namespace CouplingRefactorExercise
{
    public class OrderService
    {
        private readonly INotificationService _notificationService;
        private Database db = new Database(); // Type use coupling, can be improved by using an interface
        private NotificationService notifier = new NotificationService(); // Type use coupling, can be improved by using an interface

        public OrderService(INotificationService notificationService) // TODO: Register this in DI container
        {
            _notificationService = notificationService;
        }

        public void PlaceOrder(string product, int quantity, bool notify)
        {
            var customer = db.GetCustomer();
            db.InsertOrder(customer, product, quantity);
            Helper.Log("Order placed.");  // Common coupling and Low cohesion, but acceptable for logging, can be improved by using a logging interface

            if (notify)
            {
                notifier.SendNotification(customer); // Stamp coupling, only email id required to pass. Can be improved by passing necessary details only
                if(customer.Email is not null)_notificationService.SendNotification(customer.Email!);
            }
        }
    }
}
