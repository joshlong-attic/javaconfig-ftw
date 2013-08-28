package javaconfigftw.batch;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

public class Main {

    public static void main(String[] args) throws Throwable {
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(BatchConfiguration.class);
        ac.registerShutdownHook();

        Resource samplesResource = new ClassPathResource("/sample/a.csv");

        CustomerLoaderService customerLoaderService = ac.getBean(CustomerLoaderService.class);
        customerLoaderService.loadCustomersFrom(samplesResource.getFile());

    }

}
