package com.bil.mybatis.plugins;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

/**
 * 监控拦截器
 *
 * @author haibo.yang
 * @since 31 五月 2018
 */
@Intercepts({@Signature(
        type = Executor.class,
        method = "query",
        args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}
), @Signature(
        type = Executor.class,
        method = "update",
        args = {MappedStatement.class, Object.class}
)})
public class MetricsInterceptor implements Interceptor {
    private static final Logger LOGGER = LoggerFactory.getLogger(MetricsInterceptor.class);
    private static final String TRACE_SQL_TYPE = "$WT_sqlType";

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
        String[] strArr = mappedStatement.getId().split("\\.");
        String traceKey = strArr[strArr.length - 2] + "." + strArr[strArr.length - 1];
        String sqlType = mappedStatement.getSqlCommandType().name();
        return this.metrics(invocation, traceKey, sqlType);
    }


    @Override
    public Object plugin(Object target) {
        return target instanceof Executor ? Plugin.wrap(target, this) : target;
    }

    @Override
    public void setProperties(Properties properties) {
    }

    protected Object metrics(Invocation invocation, String methodName, String sqlType) throws Throwable {

        Object result;
        try {
            result = this.trace(invocation, methodName, sqlType);
        } catch (Throwable var9) {
            //TODO
            throw var9;
        } finally {
            //TODO
        }
        return result;
    }

    protected Object trace(Invocation invocation, String traceKey, String sqlType) throws Throwable {
//TODO
        Object result;
        try {
            result = invocation.proceed();
        } catch (Throwable var9) {
            LOGGER.error("{}", var9.getMessage(), var9);
            //TODO
            throw var9;
        } finally {
            //TODO
        }

        return result;
    }
}
