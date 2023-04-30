package com.anhtuan.bookapp.response;

public class BaseResponse {

    private int code;

    public BaseResponse() {
    }

    public BaseResponse(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
