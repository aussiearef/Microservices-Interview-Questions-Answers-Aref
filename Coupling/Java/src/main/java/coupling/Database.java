
package coupling;

public class Database {
    public void insertCustomer(Customer customer) {
        System.out.println("Customer inserted into shared DB.");
    }

    public void insertOrder(Customer customer, String product, int quantity) {
        System.out.println("Order inserted into shared DB.");
    }

    public Customer getCustomer() {
        return new Customer("John Doe", "john@example.com");
    }
}
