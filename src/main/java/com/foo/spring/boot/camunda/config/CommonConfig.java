package com.foo.spring.boot.camunda.config;

public class CommonConfig {
    
    private int age;

    public CommonConfig(int age) {
        this.age = age;
    }

    public int getAge(){
        return this.age;
    }
}
