package com.nine.finance.model;

public class BaseModel<T> {

    public static final String SUCCESS = "success";

    public String status;
    public String message;
    public T content;

}
