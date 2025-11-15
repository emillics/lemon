package com.emillics.lemon.model;

public class Result<T> {
    private T data;
    private String error;

    public Result(T data, String error) {
        this.data = data;
        this.error = error;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    @Override
    public String toString() {
        return "DBResult{" +
                "data=" + data +
                ", error='" + error + '\'' +
                '}';
    }
}
