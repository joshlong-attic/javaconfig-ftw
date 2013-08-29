package org.activiti.spring.components.config.java.impl;

import org.activiti.engine.*;
import org.activiti.spring.components.aop.ProcessStartAnnotationBeanPostProcessor;
import org.activiti.spring.components.config.StateAnnotationBeanPostProcessor;
import org.activiti.spring.components.config.java.ActivitiConfigurer;
import org.activiti.spring.components.config.java.EnableActiviti;
import org.activiti.spring.components.scope.ProcessScope;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.target.AbstractLazyCreationTargetSource;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportAware;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.Assert;

import javax.sql.DataSource;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Much of this code was lifted from the implementation of the {@code @EnableBatchProcessing} annotations
 * in the Spring Batch project.
 *
 * @author Josh Long
 */
@Configuration
@Import(ProcessScopeConfiguration.class)
public class DefaultActvitiConfiguration implements ApplicationContextAware, ImportAware {
    private final String enableActivitiClassName = EnableActiviti.class.getName();
    private final AtomicReference<ProcessEngine> processEngineAtomicReference = new AtomicReference<ProcessEngine>();
    private ApplicationContext applicationContext;
    private volatile boolean initialized = false;
    private ActivitiConfigurer configurer;

    @Bean
    public StateAnnotationBeanPostProcessor stateAnnotationBeanPostProcessor(ProcessEngine processEngine) {
        return new StateAnnotationBeanPostProcessor(processEngine);
    }

    private void log(String msg) {
        System.out.println(msg);
    }

    private void log(String msg, Object... parms) {
        log(String.format(msg, parms));
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
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

    /**
     * creates a proxy that will delegate to a reference that will be provided later on at runtime. This gives the
     * references {@code lazy} reference semantics.
     */
    protected <T> T createLazyProxy(AtomicReference<T> reference, Class<T> types) {
        ProxyFactory factory = new ProxyFactory();
        factory.setTargetSource(new ReferenceTargetSource<T>(reference));
        factory.addAdvice(new PassthruAdvice());
        factory.setInterfaces(new Class[]{types});
        return (T) factory.getProxy();
    }

    protected void initialize() throws Exception {
        if (initialized) {
            return;
        }

        if (this.configurer == null)
            this.configurer = this.buildActivitiConfigurer(
                    applicationContext.getBeansOfType(DataSource.class).values(),
                    applicationContext.getBeansOfType(ActivitiConfigurer.class).values());

        processEngineAtomicReference.set(this.configurer.processEngine());

        initialized = true;
    }

    protected ActivitiConfigurer buildActivitiConfigurer(
            Collection<DataSource> dataSourceCollection,
            Collection<ActivitiConfigurer> configurerCollection) throws Exception {

        if (configurerCollection == null || configurerCollection.isEmpty()) {
            Assert.isTrue(dataSourceCollection != null && dataSourceCollection.size() == 1, "To use the default " + ActivitiConfigurer.class.getSimpleName() + ", the context must contain precisely one DataSource, found " + (null == dataSourceCollection ? 0 : dataSourceCollection.size()));
            DataSource dataSource = dataSourceCollection.iterator().next();
            return new DefaultActivitiConfigurer(this.applicationContext, dataSource);
        } else {
            return configurerCollection.iterator().next();
        }
    }

    @Bean
    public ManagementService managementService(ProcessEngine processEngine) {
        return processEngine.getManagementService();
    }

    @Bean
    public ProcessEngine processEngine() {
        return createLazyProxy(this.processEngineAtomicReference, ProcessEngine.class);
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

    private static class PassthruAdvice implements MethodInterceptor {

        @Override
        public Object invoke(MethodInvocation invocation) throws Throwable {
            return invocation.proceed();
        }

    }

    private class ReferenceTargetSource<T> extends AbstractLazyCreationTargetSource {

        private AtomicReference<T> reference;

        public ReferenceTargetSource(AtomicReference<T> reference) {
            this.reference = reference;
        }

        @Override
        protected Object createObject() throws Exception {
            initialize();
            return reference.get();
        }
    }
}

@Configuration
class ProcessScopeConfiguration {

    @Bean
    public ProcessScope processScope(ProcessEngine processEngine) {
        ProcessScope processScope = new ProcessScope();
        processScope.setProcessEngine(processEngine);
        return processScope;
    }
}
