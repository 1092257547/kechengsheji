package com.example.mentalhealth.entities;

public class ResultData {
    private Integer code;
    private String message;
    private Object data;
    private int page;
    private int count;
    private int limit;

    public ResultData(Integer code, String message, Object data, int page, int count, int limit) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.page = page;
        this.count = count;
        this.limit = limit;
    }
    public ResultData(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public ResultData(Integer code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }
}
