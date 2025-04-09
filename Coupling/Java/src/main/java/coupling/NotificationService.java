
package coupling;

public class NotificationService {
    public void sendNotification(Customer customer) {
        System.out.println("Sending email to " + customer.getEmail());
    }
}
