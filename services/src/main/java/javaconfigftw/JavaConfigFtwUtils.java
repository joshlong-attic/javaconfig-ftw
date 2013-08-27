package javaconfigftw;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 *
 */
public abstract class JavaConfigFtwUtils {


    public static AnnotationConfigApplicationContext run(Class<?>... jcClasses) {
        AnnotationConfigApplicationContext annotationConfigApplicationContext
                = new AnnotationConfigApplicationContext(jcClasses);
        annotationConfigApplicationContext.registerShutdownHook();
        return annotationConfigApplicationContext;
    }
}
