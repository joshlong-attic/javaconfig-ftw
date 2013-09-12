
package io.spring.presentations.jcmm.javaconfig;

import io.spring.presentations.jcmm.CustomService;
import io.spring.presentations.jcmm.StupidPlatformTransactionManager;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@PropertySource("classpath:config.properties")
@EnableTransactionManagement
public class JavaConfigApplication {

	@Autowired
	private Environment environment;

	@Bean
	public CustomService myCustomService() {
		System.out.println("myCustomService()");
		CustomService customService = new CustomService();
		customService.setMessage(environment.getProperty("message"));
		customService.setDataSource(myDataSource());
		return customService;
	}

	@Bean
	public DataSource myDataSource() {
		System.out.println("myDataSource()");
		EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
		builder.setType(EmbeddedDatabaseType.HSQL);
		return builder.build();
	}

	@Bean
	public PlatformTransactionManager txManager() {
		return new StupidPlatformTransactionManager();
	}

	public static void main(String[] args) {
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(
				JavaConfigApplication.class);
		CustomService service = context.getBean("myCustomService", CustomService.class);
		service.run();
		context.close();
	}

}
