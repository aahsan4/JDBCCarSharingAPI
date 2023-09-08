package carsharing;
import java.util.*;
public interface CustomerDAO {
    void createCustomer(String name);
    List<Customer> getAllCustomers();
    Customer getCustomerById(int id);

    void updateRentedCar(int customerId, Integer rentedCarId);
}

