package javaconfigftw.qualifiers;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

public class Main {
    public static void main(String args[]) throws Throwable {
        AnnotationConfigApplicationContext annotationConfigurationApplicationContext = new AnnotationConfigApplicationContext(Config.class);
        annotationConfigurationApplicationContext.registerShutdownHook();
    }

    @Configuration
    public static class Config {

        @Bean
        public Client client() {
            return new Client();
        }

        @Bean(name = "ios")
        @IOsStore
        public ItunesBookShop itunesBookShop() {
            return new ItunesBookShop();
        }

        @Bean(name = "android")
        @AndroidStore
        public AmazonBookShop amazonBookShop() {
            return new AmazonBookShop();
        }
    }

}
