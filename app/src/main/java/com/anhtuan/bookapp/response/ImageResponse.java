package com.anhtuan.bookapp.response;

public class ImageResponse extends BaseResponse{
    String data;

    public ImageResponse(int code) {
        super(code);
    }

    public ImageResponse(int code, String data) {
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
