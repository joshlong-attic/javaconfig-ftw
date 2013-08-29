package org.activiti.spring.components.config.java.impl;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.activiti.spring.ProcessEngineFactoryBean;
import org.activiti.spring.SpringProcessEngineConfiguration;
import org.activiti.spring.components.config.java.ActivitiConfigurer;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.util.Assert;

import javax.sql.DataSource;

/**
 * @author Josh Long
 */
public class DefaultActivitiConfigurer implements ActivitiConfigurer {

    private DataSource dataSource;
    private PlatformTransactionManager platformTransactionManager;
    private ApplicationContext applicationContext;

    public DefaultActivitiConfigurer(ApplicationContext context, DataSource d) {
        setup(context, d);
    }

    public ApplicationContext applicationContext() {
        return this.applicationContext;
    }

    private void setup(ApplicationContext context, DataSource d) {
        this.applicationContext = context;
        Assert.notNull(d, "the dataSource can't be null");
        if (d instanceof TransactionAwareDataSourceProxy) {
            this.dataSource = d;
        } else {
            TransactionAwareDataSourceProxy transactionAwareDataSourceProxy =
                    new TransactionAwareDataSourceProxy(d);
            transactionAwareDataSourceProxy.afterPropertiesSet();
            this.dataSource = transactionAwareDataSourceProxy;
        }
        this.platformTransactionManager = new DataSourceTransactionManager(d);
    }

    public DataSource dataSource() {
        return this.dataSource;
    }

    public PlatformTransactionManager platformTransactionManager() {
        return this.platformTransactionManager;
    }

    public ProcessEngineConfigurationImpl processEngineConfiguration() {
        SpringProcessEngineConfiguration springProcessEngineConfiguration = new SpringProcessEngineConfiguration();
        springProcessEngineConfiguration.setDatabaseSchemaUpdate(SpringProcessEngineConfiguration.DB_SCHEMA_UPDATE_CREATE);
        springProcessEngineConfiguration.setTransactionManager(platformTransactionManager());
        springProcessEngineConfiguration.setJobExecutorActivate(false);
        springProcessEngineConfiguration.setDataSource(dataSource());
        return springProcessEngineConfiguration;
    }

    public ProcessEngine processEngine() throws Exception {
        ProcessEngineFactoryBean processEngineFactoryBean = new ProcessEngineFactoryBean();
        processEngineFactoryBean.setApplicationContext(this.applicationContext());
        processEngineFactoryBean.setProcessEngineConfiguration(processEngineConfiguration());
        return processEngineFactoryBean.getObject();
    }


}
