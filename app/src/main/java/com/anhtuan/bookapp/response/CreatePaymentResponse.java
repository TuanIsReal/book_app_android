package com.anhtuan.bookapp.response;

public class CreatePaymentResponse extends BaseResponse{
    private String data;

    public CreatePaymentResponse(int code) {
        super(code);
    }

    public CreatePaymentResponse(int code, String data) {
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
