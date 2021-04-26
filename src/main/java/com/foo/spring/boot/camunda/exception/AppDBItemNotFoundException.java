package com.foo.spring.boot.camunda.exception;

public class AppDBItemNotFoundException extends AppCommonException {
    String entity;
    String field;
    String value;

    public AppDBItemNotFoundException(EnumErrorCode errorCode, String message, Throwable cause, String entity, String field, String value) {
        super(errorCode, message, cause);
        this.entity = entity;
        this.field = field;
        this.value = value;
    }
    public AppDBItemNotFoundException(EnumErrorCode errorCode, String message, Throwable cause) {
        super(errorCode, message, cause);
    }

    public AppDBItemNotFoundException(EnumErrorCode errorCode, Throwable cause, String entity, String field, String value) {
        super(errorCode, cause);
        this.entity = entity;
        this.field = field;
        this.value = value;
    }

    public AppDBItemNotFoundException(EnumErrorCode errorCode, String entity, String field, String value) {
        super(errorCode);
        this.entity = entity;
        this.field = field;
        this.value = value;
    }

    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
