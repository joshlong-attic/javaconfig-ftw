package javaconfigftw.data.mongodb;


import javaconfigftw.CloudFoundryDataSourceConfiguration;
import javaconfigftw.Customer;
import javaconfigftw.JavaConfigFtwUtils;
import javaconfigftw.LocalDataSourceConfiguration;
import org.apache.commons.io.IOUtils;
import org.springframework.context.annotation.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Main {

    private final static Random random = new Random();

    public static void main(String[] args) throws Throwable {

        AnnotationConfigApplicationContext annotationConfigApplicationContext = JavaConfigFtwUtils.run(MongoRepositoryConfiguration.class) ;
        CustomerRepository customerRepository = annotationConfigApplicationContext.getBean(CustomerRepository.class);
        Customer[] customers = new Customer[]{
                (new Customer("Phill", "Webb")),
                (new Customer("Mark", "Pollack")),
                (new Customer("Mark", "Fisher")),
                (new Customer("Oliver", "Gierke")),
                (new Customer("Dave", "Syer")),
                (new Customer("Chris", "Beams")),
                (new Customer("Josh", "Long"))
        };

        customerRepository.deleteAll();

        for (Customer customer : customers)
            customerRepository.save(customer);

        Iterable<Customer> customerList = customerRepository.findAll();

        List<Customer> allCustomersFromDatabase = new ArrayList<Customer>();
        for (Customer customer : customerList)
            allCustomersFromDatabase.add(customer);

        Customer randomCustomer = customers[random.nextInt(allCustomersFromDatabase.size())];

        Resource resource = new ClassPathResource("/sample/photo.jpg");
        InputStream inputStream = resource.getInputStream();
        customerRepository.storeProfilePhoto(  (randomCustomer.getId()), inputStream);

        InputStream readInputStream = customerRepository.readProfilePhoto(   ( randomCustomer.getId()));

        System.out.println(
                "Do the bytes stored in MongoDB match" +
                " the byte[]s for the image we stored in Mongo? " +
                    Arrays.equals(IOUtils.toByteArray(readInputStream), (IOUtils.toByteArray(resource.getInputStream()))));

    }

    @Configuration
    @Import({LocalDataSourceConfiguration.class, CloudFoundryDataSourceConfiguration.class})
    @EnableMongoRepositories
    @ComponentScan
    public static class MongoRepositoryConfiguration {
        @Bean
        public MongoTemplate mongoTemplate(MongoDbFactory mongoDbFactory) {
            return new MongoTemplate(mongoDbFactory);
        }

        @Bean
        public GridFsTemplate gridFsTemplate(MongoDbFactory mongoDbFactory, MongoTemplate mongoTemplate) throws Exception {
            return new GridFsTemplate(mongoDbFactory, mongoTemplate.getConverter());
        }
    }

}
