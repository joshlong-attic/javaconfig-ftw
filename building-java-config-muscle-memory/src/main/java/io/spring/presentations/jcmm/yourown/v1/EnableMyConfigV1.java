
package io.spring.presentations.jcmm.yourown.v1;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;


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
