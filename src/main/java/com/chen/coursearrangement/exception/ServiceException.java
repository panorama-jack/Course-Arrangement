package com.chen.coursearrangement.exception;

import lombok.Getter;

/**
 * 自定义异常
 * 自定义异常类的设计用于在业务层抛出一些特定的异常，以便在应用程序的其他部分能够捕获并根据错误码进行适当的处理。例如，在处理业务逻辑时，如果出现了某种错误情况，可以抛出这个异常并在上层捕获并进行相应的错误处理。
 */
@Getter
public class ServiceException extends RuntimeException {
    private String code;

    public ServiceException(String code, String msg) {
        super(msg);
        this.code = code;
    }
}
