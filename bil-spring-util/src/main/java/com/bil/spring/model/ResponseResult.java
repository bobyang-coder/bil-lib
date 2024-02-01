package com.bil.spring.model;

import lombok.Data;

/**
 * #1000～1999 区间表示参数错误
 * #2000～2999 区间表示用户错误
 * #3000～3999 区间表示接口异常
 *
 * @author haibo.yang
 * @since 2022/1/10
 */
@Data
public class ResponseResult<T> {

    /**
     * 返回状态码
     */

    private Integer code;
    /**
     * 返回信息描述
     */
    private String message;
    /**
     * 返回值
     */
    private T data;


    public static <T> ResponseResult<T> success() {
        ResponseResult<T> result = new ResponseResult<T>();
        result.setCode(ResultCode.SUCCESS.getCode());
        return result;
    }

    public static <T> ResponseResult<T> success(T data) {
        ResponseResult<T> result = new ResponseResult<T>();
        result.setCode(ResultCode.SUCCESS.getCode());
        result.setData(data);
        return result;
    }

    public static <T> ResponseResult<T> fail(ResultCode code) {
        return make(code.getCode(), code.getMessage(), null);
    }

    public static <T> ResponseResult<T> fail(ResultCode code, T data) {
        return make(code.getCode(), code.getMessage(), data);
    }

    public static <T> ResponseResult<T> paramError(String message) {
        return make(ResultCode.PARAM_ERROR.getCode(), message, null);
    }

    public static <T> ResponseResult<T> make(int code, String message, T data) {
        ResponseResult<T> result = new ResponseResult<T>();
        result.setCode(code);
        result.setMessage(message);
        result.setData(data);
        return result;
    }

}
