package com.bil.spring.model;

import lombok.Getter;

/**
 * 状态码
 * #1000～1999 区间表示参数错误
 * #2000～2999 区间表示用户错误
 * #3000～3999 区间表示接口异常
 * #4000～4999 区间表示
 * #5000～5999 区间表示系统异常
 *
 * @author haibo.yang
 * @since 2022/1/10
 */
@Getter
public enum ResultCode {

    /**
     *
     */
    ERROR(-1, "失败"),
    SUCCESS(0, "成功"),

    /**
     * #1000～1999 区间表示参数错误
     */
    PARAM_ERROR(1001, "参数错误"),


    /**
     * #5000～5999 区间表示系统异常
     */
    SYSTEM_ERROR(5000, "系统异常"),
    ;

    /**
     * 返回状态码
     */

    private Integer code;
    /**
     * 返回信息描述
     */
    private String message;

    ResultCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
