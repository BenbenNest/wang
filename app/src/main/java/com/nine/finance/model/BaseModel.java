package com.nine.finance.model;

public class BaseModel<T> {

    public static final String SUCCESS = "success";
    public static final String FAIL = "fail";

    public String request_id;
    public String result_code;
    public String msg;
    public long server_time;

    public T data;

}
