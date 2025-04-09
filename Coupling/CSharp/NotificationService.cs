
using System;

namespace CouplingRefactorExercise
{
    public class NotificationService
    {
        public void SendNotification(Customer customer)
        {
            Console.WriteLine($"Sending email to {customer.Email}");
        }
    }
}
