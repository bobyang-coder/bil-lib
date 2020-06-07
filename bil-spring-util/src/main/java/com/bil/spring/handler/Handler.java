package com.bil.spring.handler;

/**
 * 处理器
 *
 * @author haibo.yang   <bobyang_coder@163.com>
 * @version v1.0.0
 * @since 2019-04-21
 */
public interface Handler<P, R> {

    /**
     * 处理业务
     *
     * @param p
     * @return
     */
    R handle(P p);
}
