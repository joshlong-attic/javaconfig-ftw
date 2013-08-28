package org.activiti.spring.components.config.java;

import org.activiti.engine.*;
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.activiti.spring.ProcessEngineFactoryBean;
import org.activiti.spring.components.aop.ProcessStartAnnotationBeanPostProcessor;
import org.activiti.spring.components.config.xml.StateHandlerAnnotationBeanFactoryPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportAware;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.Assert;

/**
 * @author Josh Long
 */
@Configuration
public abstract class DefaultActvitiConfiguration implements ImportAware {

    private final String enableActivitiClassName = EnableActiviti.class.getName();

    @Bean
    public static StateHandlerAnnotationBeanFactoryPostProcessor stateHandlerAnnotationBeanFactoryPostProcessor(ProcessEngine processEngine) {
        StateHandlerAnnotationBeanFactoryPostProcessor stateHandlerAnnotationBeanFactoryPostProcessor = new StateHandlerAnnotationBeanFactoryPostProcessor();
        stateHandlerAnnotationBeanFactoryPostProcessor.setProcessEngine(processEngine);
        return stateHandlerAnnotationBeanFactoryPostProcessor;

    }

    @Bean
    public RepositoryService repositoryService(ProcessEngine processEngine) {
        return processEngine.getRepositoryService();
    }

    @Bean
    public RuntimeService runtimeService(ProcessEngine processEngine) {
        return processEngine.getRuntimeService();
    }

    @Bean
    public TaskService taskService(ProcessEngine processEngine) {
        return processEngine.getTaskService();
    }

    @Bean
    public HistoryService historyService(ProcessEngine processEngine) {
        return processEngine.getHistoryService();
    }

    @Bean
    public ManagementService managementService(ProcessEngine processEngine) {
        return processEngine.getManagementService();
    }

    @Bean
    public ProcessEngineFactoryBean processEngine(ProcessEngineConfigurationImpl processEngineConfiguration) {
        ProcessEngineFactoryBean processEngineFactoryBean = new ProcessEngineFactoryBean();
        processEngineFactoryBean.setProcessEngineConfiguration(processEngineConfiguration);
        return processEngineFactoryBean;
    }

    @Override
    public void setImportMetadata(AnnotationMetadata importMetadata) {
        AnnotationAttributes enabled = AnnotationAttributes.fromMap(importMetadata.getAnnotationAttributes(enableActivitiClassName, false));
        Assert.notNull(enabled, "@" + this.enableActivitiClassName + " is not present on importing class " + importMetadata.getClassName());
    }

    @Bean
    public ProcessStartAnnotationBeanPostProcessor processStartAnnotationBeanPostProcessor(ProcessEngine processEngine) {
        ProcessStartAnnotationBeanPostProcessor processStartAnnotationBeanPostProcessor = new ProcessStartAnnotationBeanPostProcessor();
        processStartAnnotationBeanPostProcessor.setProcessEngine(processEngine);
        return processStartAnnotationBeanPostProcessor;
    }

    @Bean
    public ProcessStartAnnotationBeanPostProcessor processStartingAnnotationBeanPostProcessor() {
        return new ProcessStartAnnotationBeanPostProcessor();
    }

/*
    private final String processEngineAttribute = "process-engine";

    public BeanDefinition parse(Element element, ParserContext parserContext) {
        registerProcessScope(element, parserContext);
        registerStateHandlerAnnotationBeanFactoryPostProcessor(element, parserContext);
        registerProcessStartAnnotationBeanPostProcessor(element, parserContext);
        return null;
    }

    private void configureProcessEngine(AbstractBeanDefinition abstractBeanDefinition, Element element) {
        String procEngineRef = element.getAttribute(processEngineAttribute);
        if (StringUtils.hasText(procEngineRef))
            abstractBeanDefinition.getPropertyValues().add(Conventions.attributeNameToPropertyName(processEngineAttribute), new RuntimeBeanReference(procEngineRef));
    }

    private void registerStateHandlerAnnotationBeanFactoryPostProcessor(Element element, ParserContext context) {
        Class clz = StateHandlerAnnotationBeanFactoryPostProcessor.class;
        BeanDefinitionBuilder postProcessorBuilder = BeanDefinitionBuilder.genericBeanDefinition(clz.getName());

        BeanDefinitionHolder postProcessorHolder = new BeanDefinitionHolder(
                postProcessorBuilder.getBeanDefinition(),
                ActivitiContextUtils.ANNOTATION_STATE_HANDLER_BEAN_FACTORY_POST_PROCESSOR_BEAN_NAME);
        configureProcessEngine(postProcessorBuilder.getBeanDefinition(), element);
        BeanDefinitionReaderUtils.registerBeanDefinition(postProcessorHolder, context.getRegistry());

    }

    private void registerProcessScope(Element element, ParserContext parserContext) {
        Class clz = ProcessScope.class;
        BeanDefinitionBuilder processScopeBDBuilder = BeanDefinitionBuilder.genericBeanDefinition(clz);
        AbstractBeanDefinition scopeBeanDefinition = processScopeBDBuilder.getBeanDefinition();
        scopeBeanDefinition.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
        configureProcessEngine(scopeBeanDefinition, element);
        String beanName = baseBeanName(clz);
        parserContext.getRegistry().registerBeanDefinition(beanName, scopeBeanDefinition);
    }

    private void registerProcessStartAnnotationBeanPostProcessor(Element element, ParserContext parserContext) {
        Class clz = ProcessStartAnnotationBeanPostProcessor.class;

        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(clz);
        AbstractBeanDefinition beanDefinition = beanDefinitionBuilder.getBeanDefinition();
        beanDefinition.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
        configureProcessEngine(beanDefinition, element);

        String beanName = baseBeanName(clz);
        parserContext.getRegistry().registerBeanDefinition(beanName, beanDefinition);
    }

    private String baseBeanName(Class cl) {
        return cl.getName().toLowerCase();
    }*/
 /*   <bean class="org.activiti.spring.test.components.ProcessInitiatingPojo" id="pojo"/>

    <bean id="dataSource" class="org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy">
    <property name="targetDataSource">
    <bean class="org.springframework.jdbc.datasource.SimpleDriverDataSource">
    <property name="driverClass" value="org.h2.Driver"/>
    <property name="url" value="jdbc:h2:mem:activiti;DB_CLOSE_DELAY=1000"/>
    <property name="username" value="sa"/>
    <property name="password" value=""/>
    </bean>
    </property>
    </bean>

    <bean id="transactionManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
    <property name="dataSource" ref="dataSource"/>
    </bean>

    <bean id="processEngineConfiguration" class="org.activiti.spring.SpringProcessEngineConfiguration">
    <property name="dataSource" ref="dataSource"/>
    <property name="transactionManager" ref="transactionManager"/>
    <property name="databaseSchemaUpdate" value="true"/>
    <property name="jobExecutorActivate" value="false"/>
    </bean>

    <bean id="processEngine" class="org.activiti.spring.ProcessEngineFactoryBean">
    <property name="processEngineConfiguration" ref="processEngineConfiguration"/>
    </bean>

    <bean id="repositoryService" factory-bean="processEngine" factory-method="getRepositoryService"/>
    <bean id="runtimeService" factory-bean="processEngine" factory-method="getRuntimeService"/>
    <bean id="taskService" factory-bean="processEngine" factory-method="getTaskService"/>
    <bean id="historyService" factory-bean="processEngine" factory-method="getHistoryService"/>
    <bean id="managementService" factory-bean="processEngine" factory-method="getManagementService"/>
 */

    /*

    @Autowired
    private ApplicationContext context;
    @Autowired(required = false)
    private Collection<DataSource> dataSources;
    private BatchConfigurer configurer;

    @Bean
    public JobBuilderFactory jobBuilders() throws Exception {
        return new JobBuilderFactory(jobRepository());
    }

    @Bean
    public StepBuilderFactory stepBuilders() throws Exception {
        return new StepBuilderFactory(jobRepository(), transactionManager());
    }

    @Bean
    public abstract JobRepository jobRepository() throws Exception;

    @Bean
    public abstract JobLauncher jobLauncher() throws Exception;

    @Bean
    public JobRegistry jobRegistry() throws Exception {
        return new MapJobRegistry();
    }

    @Bean
    public abstract PlatformTransactionManager transactionManager() throws Exception;

    @Override
    public void setImportMetadata(AnnotationMetadata importMetadata) {
        AnnotationAttributes enabled = AnnotationAttributes.fromMap(importMetadata.getAnnotationAttributes(
                EnableBatchProcessing.class.getName(), false));
        Assert.notNull(enabled,
                "@EnableBatchProcessing is not present on importing class " + importMetadata.getClassName());
    }

    protected BatchConfigurer getConfigurer(Collection<BatchConfigurer> configurers) throws Exception {
        if (this.configurer != null) {
            return this.configurer;
        }
        if (configurers == null || configurers.isEmpty()) {
            if (dataSources == null || dataSources.isEmpty() || dataSources.size() > 1) {
                throw new IllegalStateException(
                        "To use the default BatchConfigurer the context must contain precisely one DataSource, found "
                                + (dataSources == null ? 0 : dataSources.size()));
            }
            DataSource dataSource = dataSources.iterator().next();
            DefaultBatchConfigurer configurer = new DefaultBatchConfigurer(dataSource);
            configurer.initialize();
            this.configurer = configurer;
            return configurer;
        }
        if (configurers.size() > 1) {
            throw new IllegalStateException(
                    "To use a custom BatchConfigurer the context must contain precisely one, found "
                            + configurers.size());
        }
        this.configurer = configurers.iterator().next();
        return this.configurer;
    }*/

}