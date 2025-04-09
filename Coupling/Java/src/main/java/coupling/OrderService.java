
package coupling;

public class OrderService {
    private Database db = new Database();
    private NotificationService notifier = new NotificationService();

    public void placeOrder(String product, int quantity, boolean notify) {
        Customer customer = db.getCustomer();
        db.insertOrder(customer, product, quantity);
        Helper.log("Order placed.");

        if (notify) {
            notifier.sendNotification(customer);
        }
    }
}
