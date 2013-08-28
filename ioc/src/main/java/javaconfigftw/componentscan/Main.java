package javaconfigftw.componentscan;

import org.h2.Driver;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

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

