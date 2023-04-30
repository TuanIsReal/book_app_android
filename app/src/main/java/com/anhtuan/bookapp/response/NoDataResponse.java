package com.anhtuan.bookapp.response;

public class NoDataResponse {
    private int code;
    private Object data;

    public NoDataResponse() {
    }

    public NoDataResponse(int code, Object data) {
        this.code = code;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Response{" +
                "code=" + code +
                ", data=" + data +
                '}';
    }
}
