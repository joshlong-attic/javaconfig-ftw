package javaconfigftw;

import java.math.BigInteger;
import java.util.List;

public interface CustomerService {
    Customer updateCustomer( BigInteger id, String fn, String ln);

    Customer getCustomerById(BigInteger id);

    Customer createCustomer(String fn, String ln);

    List<Customer> loadAllCustomers();


}
