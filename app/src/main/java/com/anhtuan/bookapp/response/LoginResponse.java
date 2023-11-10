package com.anhtuan.bookapp.response;

public class LoginResponse extends BaseResponse{

    private LoginData data;

    public LoginResponse() {

    }

    public LoginResponse(int code, LoginData dataResponse) {
        super(code);
        this.data = dataResponse;
    }

    public LoginResponse(LoginData data) {
        this.data = data;
    }

    public LoginData getData() {
        return data;
    }

    public void setData(LoginData data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "LoginResponse{" +
                "data=" + data +
                '}';
    }
}
