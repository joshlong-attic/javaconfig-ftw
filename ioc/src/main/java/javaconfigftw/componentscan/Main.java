package javaconfigftw.componentscan;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.h2.Driver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.inject.Inject;
import javax.sql.DataSource;

public class Main {
    public static void main(String[] args) throws Throwable {
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(Config.class);
        ac.registerShutdownHook();
    }

    @Configuration
    @PropertySource("classpath:/config.properties")
    @ComponentScan
    public static class Config {

        @Bean
        public DataSource dataSource(Environment environment) {
            Driver jdbcDriver = new Driver();
            return new SimpleDriverDataSource(
                    jdbcDriver, environment.getProperty("ds.url"));
        }
    }

}

@Service
class CustomerService {

    @Autowired
    private DataSource dataSource1;
    @Inject
    private DataSource dataSource2;
    @Resource
    private DataSource dataSource3;

    @PostConstruct
    public void analyse() throws Throwable {
        System.out.println(ToStringBuilder.reflectionToString(dataSource1));
        System.out.println(ToStringBuilder.reflectionToString(dataSource2));
        System.out.println(ToStringBuilder.reflectionToString(dataSource3));
    }
}
