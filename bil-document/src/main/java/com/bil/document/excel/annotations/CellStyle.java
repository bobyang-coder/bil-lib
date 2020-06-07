package com.bil.document.excel.annotations;

import com.bil.document.excel.data.DataFormatter;
import org.apache.commons.lang3.StringUtils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * 单元格样式
 *
 * @author haibo.yang
 * @since 17 November 2018
 */
@Target(ElementType.FIELD)
public @interface CellStyle {

    /**
     * 头名称
     */
    String header();

    /**
     * 数据格式
     *
     * @return
     */
    Class<? extends DataFormatter> dataFormatter();

    /**
     * 单元格样式
     *
     * @return
     */
    String cellStyle() default StringUtils.EMPTY;
}
