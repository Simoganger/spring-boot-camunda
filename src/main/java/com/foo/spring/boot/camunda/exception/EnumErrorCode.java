package com.foo.spring.boot.camunda.exception;

public enum EnumErrorCode {

    // Jwt errors
    ERROR_JWT_SIGNATURE_INVALID("ERRJWT00001"),
    ERROR_JWT_TOKEN_INVALID("ERRJWT00002"),
    ERROR_JWT_TOKEN_EXPIRED("ERRJWT00003"),
    ERROR_JWT_TOKEN_UNSUPPORTED("ERRJWT00004"),
    ERROR_JWT_TOKEN_EMPTY_CLAIMS("ERRJWT00005"),

    // DB errors
    ERROR_DB_ITEM_NOTFOUND("ERRDB00001"),
    ERROR_DB_ITEM_ALREADY_EXIST("ERRDB00002");

    private String  code;

    EnumErrorCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

}
