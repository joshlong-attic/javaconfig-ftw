package org.activiti.spring.components.config.java;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;


/**
 * Java configuration DSL for Activiti configuration.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(ActivitiConfigurationSelector.class)
public @interface EnableActiviti {}
