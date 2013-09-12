package io.spring.presentations.jcmm.yourown.v3;

import io.spring.presentations.jcmm.CustomService;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;


public class MyConfigImportBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar, EnvironmentAware {

	private Environment environment;

	public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata,
			BeanDefinitionRegistry registry) {
		if(this.environment.acceptsProfiles("default")) {
			AnnotationAttributes attributes = AnnotationAttributes.fromMap(
					importingClassMetadata.getAnnotationAttributes(EnableMyConfigV3.class.getName()));
			String message = attributes.getString("value");
			RootBeanDefinition beanDefinition = new RootBeanDefinition(CustomService.class);
			beanDefinition.getPropertyValues().add("message", message);
			registry.registerBeanDefinition("myCustomService", beanDefinition);
		}
	}

	public void setEnvironment(Environment environment) {
		this.environment = environment;
	}

}
