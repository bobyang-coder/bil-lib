package com.bil.spring.handler;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by bob on 2019-04-21.
 *
 * @author haibo.yang   <bobyang_coder@163.com>
 * @version v1.0.0
 * @since 2019-04-21
 */
public class HandlerContext {

    private volatile ConcurrentHashMap<String, Map<String, Handler>> handlerMapping;

    private void addHandler(String group, String type) {
        checkParam(group, type);
        Map<String, Handler> handlerMap = handlerMapping.get(group);
        if (Objects.isNull(handlerMap)) {
            handlerMap = new HashMap<>();
            handlerMapping.put(group, handlerMap);
        }
        Handler handler = handlerMap.get(type);
        if (Objects.nonNull(handler)) {
            throw new IllegalArgumentException(String.format(" handler(group:%s,type:%s) is exist", group, type));
        }
    }

    private Handler getHandler(String group, String type) {
        checkParam(group, type);
        Map<String, Handler> map = handlerMapping.get(group);
        if (Objects.isNull(map) || map.isEmpty()) {
            throw new IllegalArgumentException("not handler");
        }
        Handler handler = map.get(type);
        if (Objects.isNull(handler)) {
            throw new IllegalArgumentException("not handler");
        }
        return handler;
    }


    private void checkParam(String group, String type) {
        if (Objects.isNull(group)) {
            throw new IllegalArgumentException("handler group is null");
        }
        if (Objects.isNull(type)) {
            throw new IllegalArgumentException("handler type is null");
        }
    }

}
