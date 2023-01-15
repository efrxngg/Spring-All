package edu.spring.core.util;

public class ErrorMessage {
    private String msg;
    private String path;

    public ErrorMessage() {
    }

    public ErrorMessage(String msg, String path) {
        this.msg = msg;
        this.path = path;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
