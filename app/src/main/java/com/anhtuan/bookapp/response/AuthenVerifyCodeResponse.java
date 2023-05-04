package com.anhtuan.bookapp.response;

public class AuthenVerifyCodeResponse extends BaseResponse{
    private String data;

    public AuthenVerifyCodeResponse(int code) {
        super(code);
    }

    public AuthenVerifyCodeResponse(int code, String data) {
        super(code);
        this.data = data;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
