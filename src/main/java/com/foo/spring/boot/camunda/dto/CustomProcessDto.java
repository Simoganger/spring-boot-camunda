package com.foo.spring.boot.camunda.dto;

import javax.validation.constraints.NotBlank;

public class CustomProcessDto {

    @NotBlank
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
