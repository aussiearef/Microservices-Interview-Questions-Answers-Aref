
using System;

namespace CouplingRefactorExercise
{
    class Program
    {
        static void Main(string[] args)
        {
            var orderService = new OrderService();
            orderService.PlaceOrder("Laptop", 1, true);
        }
    }
}
