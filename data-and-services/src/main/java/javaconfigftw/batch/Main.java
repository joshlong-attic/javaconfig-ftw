package javaconfigftw.batch;

import javaconfigftw.JavaConfigFtwUtils;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

public class Main {

    public static void main(String[] args) throws Throwable {
        AnnotationConfigApplicationContext annotationConfigApplicationContext = JavaConfigFtwUtils.run(BatchConfiguration.class) ;

        Resource samplesResource = new ClassPathResource("/sample/a.csv");

        CustomerLoaderService customerLoaderService = annotationConfigApplicationContext.getBean(CustomerLoaderService.class);
        customerLoaderService.loadCustomersFrom(samplesResource.getFile());

    }

}
