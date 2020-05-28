package com.v2hoping.common;

/**
 * Created by houping wang on 2020/5/12
 *
 * @author houping wang
 */
public class HopingException extends RuntimeException{

    public HopingException() {
        super();
    }

    public HopingException(String message) {
        super(message);
    }

    public HopingException(String message, Throwable cause) {
        super(message, cause);
    }

    public HopingException(Throwable cause) {
        super(cause);
    }

    protected HopingException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
