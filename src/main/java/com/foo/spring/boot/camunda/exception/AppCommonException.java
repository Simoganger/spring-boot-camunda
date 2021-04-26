package com.foo.spring.boot.camunda.exception;

public class AppCommonException extends Exception {

    private EnumErrorCode errorCode;
    private Object data;

    public AppCommonException(EnumErrorCode errorCode, String message, Object data, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
        this.data = data;
    }

    public AppCommonException(EnumErrorCode errorCode, Object data, Throwable cause) {
        super(cause);
        this.errorCode = errorCode;
        this.data = data;
    }

    public AppCommonException(EnumErrorCode errorCode, String message, Throwable cause) {
        super(message,cause);
        this.errorCode = errorCode;
    }

    public AppCommonException(EnumErrorCode errorCode, Object data) {
        this.errorCode = errorCode;
        this.data = data;
    }

    public AppCommonException(EnumErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public EnumErrorCode getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(EnumErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
