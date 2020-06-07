package com.bil.lang.stream;

import org.apache.commons.collections4.CollectionUtils;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Stream操作相关工具类
 *
 * @author haibo.yang
 * @since 2020/05/07
 */
public class StreamUtils {

    /**
     * 将List转成Map
     *
     * @param collection  目标collection
     * @param keyMapper   获取key的function
     * @param valueMapper 获取value的function
     * @param <E>         集合元素
     * @param <K>         返回值Map的key类型
     * @param <V>         返回值Map的value类型
     * @return
     */
    public static <E, K, V> Map<K, V> toMapIgnoreDuplicateKey(Collection<E> collection,
                                                              Function<E, K> keyMapper,
                                                              Function<E, V> valueMapper) {
        if (CollectionUtils.isEmpty(collection) || Objects.isNull(keyMapper)) {
            return new HashMap<>(1);
        }
        return collection.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(keyMapper, valueMapper, (d1, d2) -> d1));
    }
}
