package javaconfigftw;

import java.util.List;

/**
 * @author Josh Long
 */
public  interface SearchCapableCustomerService extends CustomerService {
    List<Customer> search (String query) ;
}
