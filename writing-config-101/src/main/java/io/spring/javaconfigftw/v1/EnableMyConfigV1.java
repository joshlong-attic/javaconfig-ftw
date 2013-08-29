
package io.spring.javaconfigftw.v1;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Import;

/**
 * In the simplest form you can just {@code @Import} configuration. There is a protected
 * method in {@link SimpleMessageConfig} so you could also use it as the basis of your own
 * configuration.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(SimpleMessageConfig.class)
public @interface EnableMyConfigV1 {

}
