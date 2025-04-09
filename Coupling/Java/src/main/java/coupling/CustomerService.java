
package coupling;

public class CustomerService {
    private Database db = new Database();

    public void registerCustomer(String name, String email) {
        Customer customer = new Customer(name, email);
        db.insertCustomer(customer);
    }
}
