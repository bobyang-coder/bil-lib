package com.bil.spring.handler.support;

import com.bil.spring.handler.Handler;
import com.bil.spring.handler.HandlerContext;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Objects;

/**
 * Created by bob on 2019-04-21.
 *
 * @author haibo.yang   <bobyang_coder@163.com>
 * @version v1.0.0
 * @since 2019-04-21
 */
public class HandlerTypeBeanPostProcessor implements BeanPostProcessor, ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public Object postProcessBeforeInitialization(Object o, String s) throws BeansException {
        return null;
    }

    @Override
    public Object postProcessAfterInitialization(Object o, String s) throws BeansException {
        if (!(o instanceof Handler)) {
            return o;
        }

        HandlerContext handlerContext = applicationContext.getBean(HandlerContext.class);
        if (Objects.isNull(handlerContext)) {
            handlerContext = new HandlerContext();
//            applicationContext.
        }
        return o;
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
