package org.activiti.spring.components.config.java;

import org.activiti.engine.ProcessEngine;

/**
 * callback for any configurations that want to override the defaults
 * @author Josh Long
 */
public interface ActivitiConfigurer {



    ProcessEngine processEngine() throws Exception ;

}
