package com.foo.spring.boot.camunda.model;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
public class CustomProcess extends Model {

    private String name;
    private String processId;

    @OneToOne(targetEntity = File.class)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private File bpmnFile;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public File getBpmnFile() {
        return bpmnFile;
    }

    public void setBpmnFile(File bpmnFile) {
        this.bpmnFile = bpmnFile;
    }

    public String getProcessId() {
        return processId;
    }

    public void setProcessId(String processId) {
        this.processId = processId;
    }

    @Override
    public String toString() {
        return "Workflow{" +
                "name='" + name + '\'' +
                ", processId='" + processId + '\'' +
                ", bpmnFile=" + bpmnFile +
                '}';
    }
}
