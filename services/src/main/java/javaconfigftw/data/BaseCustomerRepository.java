package javaconfigftw.data;

import javaconfigftw.Customer;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.math.BigInteger;
import java.util.List;

public interface BaseCustomerRepository extends PagingAndSortingRepository<Customer, BigInteger> {

    List<Customer> findByFirstName(String firstName);

    List<Customer> findByFirstName(String firstName, Pageable pageable);

    List<Customer> findByFirstNameAndLastName(String firstName, String lastName);

    List<Customer> findByFirstNameAndLastName(String firstName, String lastName, Pageable pageable);

}
