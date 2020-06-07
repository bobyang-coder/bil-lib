package com.bil.document.excel.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * 文件样式
 *
 * @author haibo.yang
 * @since 17 November 2018
 */
@Target(ElementType.FIELD)
public @interface FileStyle {

    /**
     * 每页数据量
     *
     * @return
     */
    int sheetSize() default 200000;


    /**
     * 文件格式
     *
     * @return
     */
    String fileType() default "XLS";
}
