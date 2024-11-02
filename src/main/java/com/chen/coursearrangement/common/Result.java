package com.chen.coursearrangement.common;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 接口统一返回包装类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result {
    //返回包装类
    private String code;
    private String msg;
    private Object data;

    //都不返回
    public static Result success() {
        return new Result(ConstantInfo.CODE_200, "", null);
    }

    //返回信息
    public static Result success(String msg) {
        return new Result(ConstantInfo.CODE_200, msg, null);
    }

    //仅返回数据
    public static Result success(Object data) {
        return new Result(ConstantInfo.CODE_200, "", data);
    }

    //返回信息和数据
    public static Result success(String msg, Object data) {
        return new Result(ConstantInfo.CODE_200, msg, data);
    }

    //系统错误，并返回错误信息
    public static Result error(String msg) {
        return new Result(ConstantInfo.CODE_500, msg, null);
    }

    //返回错误代码和信息
    public static Result error(String code, String msg) {
        return new Result(code, msg, null);
    }


}
