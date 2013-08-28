package org.activiti.spring.components.config.java;

import org.activiti.engine.ProcessEngine;

import javax.sql.DataSource;

/**
 * @author Josh Long
 */
public class DefaultActivitiConfigurer  implements ActivitiConfigurer {
    @Override
    public ProcessEngine processEngine() throws Exception {


        return null ;
    }

    public DefaultActivitiConfigurer(DataSource dataSource) {
     this.dataSource = dataSource;
    }

    private DataSource dataSource  ;
}
