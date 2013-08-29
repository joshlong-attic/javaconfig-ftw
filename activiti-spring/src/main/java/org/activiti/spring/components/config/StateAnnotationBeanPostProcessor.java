package org.activiti.spring.components.config;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.spring.annotations.ProcessVariable;
import org.activiti.spring.annotations.State;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.reflect.MethodUtils;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.util.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This class simply creates proxies that in turn dispatch to methods on the class based on the metadata provided by their annotation
 *
 * @author Josh Long
 */
public class StateAnnotationBeanPostProcessor implements BeanPostProcessor {

    private ProcessEngine processEngine;
    private Map<String, HandlerRegistration> mapOfStateHandlers = new ConcurrentHashMap<String, HandlerRegistration>();

    public StateAnnotationBeanPostProcessor(ProcessEngine processEngine) {
        this.processEngine = processEngine;
    }

    private void ensureHandlerRegistered(final String serviceTask, final Object o) {
        if (!mapOfStateHandlers.containsKey(serviceTask)) {

            ReflectionUtils.doWithMethods(
                    o.getClass(),
                    new ReflectionUtils.MethodCallback() {
                        @Override
                        public void doWith(Method method) throws IllegalArgumentException, IllegalAccessException {
                            mapOfStateHandlers.put(serviceTask, new HandlerRegistration(serviceTask, method));
                        }
                    },
                    new ReflectionUtils.MethodFilter() {
                        @Override
                        public boolean matches(Method method) {
                            return handlesEvent(serviceTask, method);
                        }

                        private boolean handlesEvent(String e, Method method) {
                            State stateAnnotationForCurrentMethod = null;
                            boolean isStateHandlerMethod =
                                    method.getAnnotations().length > 0 && (stateAnnotationForCurrentMethod = method.getAnnotation(State.class)) != null;
                            if (isStateHandlerMethod) {
                                String stateAssignedTo = StringUtils.defaultIfBlank(
                                        stateAnnotationForCurrentMethod.state(), stateAnnotationForCurrentMethod.value());
                                if (e.equals(stateAssignedTo)) {
                                    return true;
                                }
                            }
                            return false;
                        }
                    }
            );
        }
    }

    protected int positionOfDelegateExecution(Method handlerMethod) {
        int pos = 0;
        for (Class<?> argClass : handlerMethod.getParameterTypes()) {
            if (argClass.isAssignableFrom(DelegateExecution.class)) {
                return pos;
            }
            pos += 1;
        }
        return -1;
    }

    protected void delegateTo(Object o, DelegateExecution delegateExecution) throws Exception {

        String serviceTaskIdToWhichWereResponding = delegateExecution.getCurrentActivityId();
        ensureHandlerRegistered(serviceTaskIdToWhichWereResponding, o);

        HandlerRegistration handlerRegistration = this.mapOfStateHandlers.get(serviceTaskIdToWhichWereResponding);
        Method handlerMethod = handlerRegistration.getMethod();

        Map<Integer, String> processVariableArgumentPositions = whatProcessVariablesAreExpectedAndWhere(handlerMethod);
        int positionCounter = positionOfDelegateExecution(handlerMethod);

        // todo some sort of validation to check that there are no arguments that we can't handle
        int argLength = handlerMethod.getParameterTypes().length;
        Object[] runtimeParameterValues = new Object[argLength];
        for (int indx : processVariableArgumentPositions.keySet()) {
            runtimeParameterValues[indx] = processVariable(processVariableArgumentPositions.get(indx), handlerMethod.getParameterTypes()[indx]);
        }
        if (positionCounter != -1) {
            runtimeParameterValues[positionCounter] = delegateExecution;
        }
        handlerMethod.invoke(o, runtimeParameterValues);


    }

    private Object processVariable(String variableName, Class<?> typeOfProcessVariable) {
        ProcessInstance processInstance = Context.getExecutionContext().getProcessInstance();
        ExecutionEntity executionEntity = (ExecutionEntity) processInstance;
        return executionEntity.getVariable(variableName);
    }

    protected Map<Integer, String> whatProcessVariablesAreExpectedAndWhere(Method handlerMethod) {
        Map<Integer, String> whatProcessVariablesAreExpectedAndWhere = new HashMap<Integer, String>();
        Annotation[][] paramAnnotations = handlerMethod.getParameterAnnotations();
        int offset = 0;
        for (Annotation[] annotationsForAGivenParameter : paramAnnotations) {
            for (Annotation a : annotationsForAGivenParameter) {
                if (a instanceof ProcessVariable) {
                    ProcessVariable processVariable = (ProcessVariable) a;
                    String pvName = processVariable.value();
                    whatProcessVariablesAreExpectedAndWhere.put(offset, pvName);
                }
            }
            offset += 1;
        }
        return whatProcessVariablesAreExpectedAndWhere;
    }

    protected JavaDelegate javaDelegateFromExistingBean(final Object o) throws BeansException {
        // first create a proxy that implements JavaDelegate
        // then setup logic to dispatch to a cached metadata map containing the methods on the class as well as
        // the particular runtime arguments those methods require, including access to things like @ProcessVariable annotations

        if (o instanceof JavaDelegate) {
            return (JavaDelegate) o;
        }

        final Method javaDelegateExecutionMethod =
                MethodUtils.getAccessibleMethod(JavaDelegate.class, "execute", DelegateExecution.class);

        ProxyFactory proxyFactory = new ProxyFactory(o);
        proxyFactory.setProxyTargetClass(true);
        proxyFactory.addInterface(JavaDelegate.class);
        proxyFactory.addAdvice(new MethodInterceptor() {
            @Override
            public Object invoke(MethodInvocation invocation) throws Throwable {
                // if its one type of method handle it our selves
                // by looking up the appropriate handler method and invoking it dynamically

                Method method = invocation.getMethod();

                // either its a request we can handle
                if (method.equals(javaDelegateExecutionMethod)) {
                    DelegateExecution delegateExecution = (DelegateExecution) invocation.getArguments()[0];
                    delegateTo(o, delegateExecution);
                    return null;
                }

                // otherwise pass thru
                return invocation.proceed();
            }
        });
        return (JavaDelegate) proxyFactory.getProxy();
    }

    @Override
    public Object postProcessBeforeInitialization(Object o, String s) throws BeansException {
        return javaDelegateFromExistingBean(o);
    }

    @Override
    public Object postProcessAfterInitialization(Object o, String s) throws BeansException {
        return o;
    }

    public static class HandlerRegistration {

        private String id;
        private Method method;

        public HandlerRegistration(String id, Method toInvoke) {
            this.id = id;
            this.method = toInvoke;
        }

        public Method getMethod() {
            return this.method;
        }

        public String getId() {
            return this.id;
        }
    }
}