package com.yuqiangdede.common.dto.output;

public class HttpResult<T> {

    private String code;
    private String msg;
    private T data;

    public HttpResult() {
    }

    public HttpResult(String code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public HttpResult(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public HttpResult(boolean success, T data) {
        this.code = success ? "0" : "-1";
        this.data = data;
    }

    public HttpResult(boolean success, String msg, T data) {
        this.code = success ? "0" : "-1";
        this.msg = msg;
        this.data = data;
    }

    public HttpResult(boolean success, String msg) {
        this.code = success ? "0" : "-1";
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
