package javaconfigftw.integration;


import javaconfigftw.JavaConfigFtwUtils;
import javaconfigftw.batch.BatchConfiguration;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.integration.launch.JobLaunchingMessageHandler;
import org.springframework.context.annotation.*;


public class Main {
    public static void main(String[] args) throws Throwable {
        JavaConfigFtwUtils.run(IntegrationConfiguration.class ) ;
    }

    @Configuration
    @Import(BatchConfiguration.class)
    @ComponentScan
    @ImportResource("/javaconfigftw/integration/context.xml")
    public static class IntegrationConfiguration {
        @Bean
        public JobLaunchingMessageHandler jobMessageHandler(JobLauncher jobLauncher) throws Exception {
            return new JobLaunchingMessageHandler(jobLauncher);
        }
    }
}
