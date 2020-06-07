package com.bil.document.excel.data;

/**
 * 数据格式化
 * <p>
 * 设计模式：策略模式
 *
 * @author haibo.yang
 * @since 17 November 2018
 */
public interface DataFormatter {

    /**
     * 格式化数据
     *
     * @param source
     * @return
     */
    default Object format(Object source) {
        if (isSupport(source)) {
            return doFormat(source);
        }
        return source;
    }


    /**
     * 数据是否可以处理
     *
     * @param source
     * @return
     */
    boolean isSupport(Object source);


    /**
     * 执行格式化
     *
     * @param source
     * @return
     */
    Object doFormat(Object source);
}
