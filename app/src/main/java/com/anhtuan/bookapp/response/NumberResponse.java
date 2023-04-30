package com.anhtuan.bookapp.response;

public class NumberResponse extends BaseResponse{
    private int data;

    public NumberResponse(int code, int data) {
        super(code);
        this.data = data;
    }

    public int getData() {
        return data;
    }

    public void setData(int data) {
        this.data = data;
    }
}
