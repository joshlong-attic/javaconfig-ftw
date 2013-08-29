package org.activiti.spring.components.config.java;

import org.activiti.engine.ProcessEngine;

/**
 * callback for any configurations that want to override the defaults
 * @author Josh Long
 */
public interface ActivitiConfigurer {

    /***
     * Build up the {@link ProcessEngine process engine}. The process engine
     * itself will be used to factory other components like the {@link org.activiti.engine.RuntimeService runtime service}.
     *
     * @throws Exception
     */
    ProcessEngine processEngine() throws Exception ;

}
