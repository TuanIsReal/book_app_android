package com.anhtuan.bookapp.response;

public class RefreshTokenResponse extends BaseResponse{
    private String data;

    public RefreshTokenResponse(int code) {
        super(code);
    }

    public RefreshTokenResponse(int code, String data) {
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
