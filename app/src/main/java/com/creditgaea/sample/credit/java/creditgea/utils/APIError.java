package com.creditgaea.sample.credit.java.creditgea.utils;

public class APIError {

    private int statusCode;
    private String message;


    public APIError() {
    }

    public int status() {
        return statusCode;
    }

    public String message() {
        return message;
    }
}