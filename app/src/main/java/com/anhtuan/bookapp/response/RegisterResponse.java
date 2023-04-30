package com.anhtuan.bookapp.response;

public class RegisterResponse extends BaseResponse{

    private RegisterData data;

    public RegisterResponse(RegisterData data) {
        this.data = data;
    }

    public RegisterResponse(int code, RegisterData data) {
        super(code);
        this.data = data;
    }

    public RegisterData getData() {
        return data;
    }

    public void setData(RegisterData data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "RegisterResponse{" +
                "data=" + data +
                '}';
    }
}
