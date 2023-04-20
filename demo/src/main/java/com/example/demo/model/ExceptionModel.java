package com.example.demo.model;

public class ExceptionModel {
    private String exception;


    public ExceptionModel(Exception ex) {
        this.exception = ex.getMessage();
    }

    public String getException() {
        return exception;
    }

    public void setException(String exception) {
        this.exception = exception;
    }
}
