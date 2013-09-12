
package io.spring.presentations.jcmm.xmlconfig;

import io.spring.presentations.jcmm.CustomService;

import org.springframework.context.support.GenericXmlApplicationContext;

public class XmlConfigApplication {

	public static void main(String[] args) {
		GenericXmlApplicationContext context = new GenericXmlApplicationContext(
				"classpath:spring-config.xml");
		CustomService service = context.getBean("myCustomService", CustomService.class);
		service.run();
		context.close();
	}
}
