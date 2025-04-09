
package coupling;

public class Customer {
    private String name;
    private String email;
    private String address;
    private String phoneNumber;

    public Customer(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public String getName() { return name; }
    public String getEmail() { return email; }
}
