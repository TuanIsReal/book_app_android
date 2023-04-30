package com.anhtuan.bookapp.response;

public class CheckPurchasedBookResponse extends BaseResponse{
    private int data;

    public CheckPurchasedBookResponse(int code) {
        super(code);
    }

    public CheckPurchasedBookResponse(int code, int data) {
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
