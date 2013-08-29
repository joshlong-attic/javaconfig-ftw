package org.activiti.spring.test.components.config.java;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.spring.annotations.ProcessVariable;
import org.activiti.spring.annotations.StartProcess;
import org.activiti.spring.annotations.State;
import org.activiti.spring.components.config.java.EnableActiviti;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;

/**
 * @author Josh Long
 */
public class Main {

    public static void main(String[] args) throws Throwable {

        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext();
        ac.register(ActivitiConfiguration.class);
        ac.refresh();
        ac.registerShutdownHook();

        CustomerService customerService = ac.getBean(CustomerService.class);
        RepositoryService repositoryService = ac.getBean(RepositoryService.class);
        TaskService taskService = ac.getBean(TaskService.class);
        RuntimeService runtimeService = ac.getBean(RuntimeService.class);


        repositoryService.createDeployment()
                .addClasspathResource("org/activiti/spring/test/components/javaconfig-1.bpmn20.xml")
                .deploy();

        String processInstanceId = customerService.launchWaiterProcess(232L);


    }



    @Configuration
    @EnableActiviti
    public static class ActivitiConfiguration {

        @Bean
        public DataSource dataSource() {
            return new EmbeddedDatabaseBuilder()
                    .setType(EmbeddedDatabaseType.H2)
                    .build();
        }

        @Bean
        @Scope("process")
        public Cart cart(@Value("#{processVariables['customerId']}") long customerId) {
            return new Cart(customerId, 0);
        }

        @Bean
        public CustomerService customerService(ProcessEngine processEngine, RepositoryService repositoryService, TaskService taskService, Cart cart) {
            return new CustomerService(processEngine, repositoryService, taskService, cart);
        }
    }

    public static class CustomerService {

        private ProcessEngine processEngine;
        private RepositoryService repositoryService;
        private Cart cart;
        private TaskService taskService;

        // NB: do NOT remove this. We need it for cglib proxies!
        CustomerService() {
        }

        public CustomerService(ProcessEngine processEngine, RepositoryService repositoryService, TaskService taskService, Cart cart) {
            this.processEngine = processEngine;
            this.taskService = taskService;
            this.cart = cart;
            this.repositoryService = repositoryService;
        }

        @StartProcess(processKey = "crm-order-fulfillment", returnProcessInstanceId = true)
        public String launchWaiterProcess(@ProcessVariable("customerId") long customerId) {
            cart.amountDue = cart.amountDue + 10;
            System.out.println("start scoped 'waiter' process with customerId = " + customerId);
            return null;
        }

        @State("customer-order-review")
        public void customerOrderReview (@ProcessVariable("customerId") long customerId , DelegateExecution delegateExecution ) {
            System.out.println(this.cart.toString());
            System.out.println( "the current customerId is " + customerId+ ".") ;
           // System.out.println( delegateExecution);
        }

    }

}
