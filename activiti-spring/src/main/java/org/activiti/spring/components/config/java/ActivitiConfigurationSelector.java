package org.activiti.spring.components.config.java;

import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.Assert;

/**
 * Enables the same functionality provided by the XML {@code <act:annotation-driven/>} while employing some
 * extra convenience to auto detect required dependencies by type.
 *
 * @author Josh Long
 */
public class ActivitiConfigurationSelector implements ImportSelector {

    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {

        Class<?> annotationType = EnableActiviti.class;

        AnnotationAttributes attributes = AnnotationAttributes.fromMap(importingClassMetadata.getAnnotationAttributes(annotationType.getName(), false));

        Assert.notNull(attributes, String.format("@%s is not present on importing class '%s' as expected", annotationType.getSimpleName(), importingClassMetadata.getClassName()));

        return new String[]{DefaultActvitiConfiguration.class.getName()};
     }

}