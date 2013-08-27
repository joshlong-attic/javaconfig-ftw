package javaconfigftw.javaconfig;

import org.h2.Driver;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

import javax.sql.DataSource;

public class Main {
    public static void main(String[] args) throws Throwable {
        AnnotationConfigApplicationContext annotationConfigApplicationContext
                = new AnnotationConfigApplicationContext(Config.class);
    }


    /**
     * simple Java configuration class
     */
    @Configuration
    @PropertySource("classpath:/config.properties")
    public static class Config {

        @Bean
        public DataSource dataSource(Environment environment) {
            Driver jdbcDriver = new Driver();
            return new SimpleDriverDataSource(
                    jdbcDriver, environment.getProperty("ds.url"));
        }

        @Bean
        public CustomerService customerService(DataSource dataSource) {
            CustomerService customerService = new CustomerService();
            customerService.setDataSource(dataSource);
            return customerService;
        }
    }


}
