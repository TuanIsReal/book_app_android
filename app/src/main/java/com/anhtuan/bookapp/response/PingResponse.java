package com.anhtuan.bookapp.response;

public class PingResponse extends BaseResponse{
    private String data;

    public PingResponse(int code) {
        super(code);
    }

    public PingResponse(int code, String data) {
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
