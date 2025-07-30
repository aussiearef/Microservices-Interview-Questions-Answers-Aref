
using System;

namespace CouplingRefactorExercise
{
    public interface INotificationService
    {
        void SendNotification(string email);
    }

    public class NotificationService
    {
        public void SendNotification(Customer customer)
        {
            Console.WriteLine($"Sending email to {customer.Email}");
        }
    }

    public class EmailNotificationService : INotificationService
    {
        public void SendNotification(string email)
        {
            Console.WriteLine($"Sending email to {email}");
        }
    }
}
