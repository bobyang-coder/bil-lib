package com.bil.spring.handler;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;

/**
 * Created by bob on 2019-04-21.
 *
 * @author haibo.yang   <bobyang_coder@163.com>
 * @version v1.0.0
 * @since 2019-04-21
 */
@Target({TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface HandlerType {

    /**
     * 必传，对handler进行分组管理
     *
     * @return
     */
    String group() default "";

    /**
     * 必传，handler对应的类型，每组中类型不允许重复
     *
     * @return
     */
    String type() default "";

    /**
     * 可选，默认为类名
     *
     * @return
     */
    String name() default "";

}
