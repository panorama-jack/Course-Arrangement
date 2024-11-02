package com.chen.coursearrangement.exception;

import cn.dev33.satoken.exception.*;
import com.chen.coursearrangement.common.ConstantInfo;
import com.chen.coursearrangement.common.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 如果抛出的的是ServiceException，则调用该方法
     * @param se 业务异常
     * @return Result
     */
    @ExceptionHandler(ServiceException.class)
    @ResponseBody
    public Result handle(ServiceException se){
        return Result.error(se.getCode(), se.getMessage());
    }


    // 拦截：未登录异常
    @ExceptionHandler(NotLoginException.class)
    public Result handlerException(NotLoginException e) {

        // 打印堆栈，以供调试
        e.printStackTrace();

        // 返回给前端
        return Result.error(ConstantInfo.CODE_402,e.getMessage());
    }

    // 拦截：缺少权限异常
    @ExceptionHandler(NotPermissionException.class)
    public Result handlerException(NotPermissionException e) {
        e.printStackTrace();
        return Result.error(ConstantInfo.CODE_401,"缺少权限：" + e.getPermission());
    }

    // 拦截：缺少角色异常
    @ExceptionHandler(NotRoleException.class)
    public Result handlerException(NotRoleException e) {
        e.printStackTrace();
        return Result.error(ConstantInfo.CODE_401,"缺少角色：" + e.getRole());
    }

    // 拦截：二级认证校验失败异常
    @ExceptionHandler(NotSafeException.class)
    public Result handlerException(NotSafeException e) {
        e.printStackTrace();
        return Result.error(ConstantInfo.CODE_401,"二级认证校验失败：" + e.getService());
    }

    // 拦截：服务封禁异常 
    @ExceptionHandler(DisableServiceException.class)
    public Result handlerException(DisableServiceException e) {
        e.printStackTrace();
        return Result.error("当前账号 " + e.getService() + " 服务已被封禁 (level=" + e.getLevel() + ")：" + e.getDisableTime() + "秒后解封");
    }

    // 拦截：Http Basic 校验失败异常 
    @ExceptionHandler(NotBasicAuthException.class)
    public Result handlerException(NotBasicAuthException e) {
        e.printStackTrace();
        return Result.error(e.getMessage());
    }

    // 拦截：其它所有异常
    @ExceptionHandler(Exception.class)
    public Result handlerException(Exception e) {
        e.printStackTrace();
        return Result.error(e.getMessage());
    }

}