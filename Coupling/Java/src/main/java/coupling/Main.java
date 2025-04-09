
package coupling;

public class Main {
    public static void main(String[] args) {
        OrderService orderService = new OrderService();
        orderService.placeOrder("Laptop", 1, true);
    }
}
