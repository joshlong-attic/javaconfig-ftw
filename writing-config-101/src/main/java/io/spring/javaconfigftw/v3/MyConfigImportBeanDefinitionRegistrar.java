package io.spring.javaconfigftw.v3;

import io.spring.javaconfigftw.SimpleMessageBean;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;


public class MyConfigImportBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar, EnvironmentAware {

	private Environment environment;

	@Override
	public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata,
			BeanDefinitionRegistry registry) {
		if(this.environment.acceptsProfiles("default")) {
			AnnotationAttributes attributes = AnnotationAttributes.fromMap(
					importingClassMetadata.getAnnotationAttributes(EnableMyConfigV3.class.getName()));
			String message = attributes.getString("value");
			RootBeanDefinition beanDefinition = new RootBeanDefinition(SimpleMessageBean.class);
			beanDefinition.getConstructorArgumentValues().addGenericArgumentValue(message);
			registry.registerBeanDefinition("simpleMessageBean", beanDefinition);
		}
	}

	@Override
	public void setEnvironment(Environment environment) {
		this.environment = environment;
	}

}
