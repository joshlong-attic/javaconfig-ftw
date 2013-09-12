package io.spring.presentations.jcmm.yourown;

import io.spring.presentations.jcmm.CustomService;
import io.spring.presentations.jcmm.yourown.v3.EnableMyConfigV3;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;

@Configuration
// @EnableMyConfigV1
// @EnableMyConfigV2(showCopyright=true)
@EnableMyConfigV3("Well Hello")
public class YouOwnConfigApplication {

	public static void main(String[] args) {
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(
				YouOwnConfigApplication.class);
		CustomService service = context.getBean("myCustomService", CustomService.class);
		service.run();
		context.close();
	}

}
