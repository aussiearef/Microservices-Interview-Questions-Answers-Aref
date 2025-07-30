
using System;

namespace CouplingRefactorExercise
{
    class Program
    {
        static void Main(string[] args)
        {
            var orderService = new OrderService(); // Type use coupling, can be improved by using an interface or dependency injection.
            orderService.PlaceOrder("Laptop", 1, true); // Control coupling, here passing the boolean flag to control the flow of notification.
                                                        // Can be improved by using a more structured approach like an enum or a notification type.
        }
    }
}
