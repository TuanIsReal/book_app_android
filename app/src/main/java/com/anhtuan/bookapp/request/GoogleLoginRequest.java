package com.anhtuan.bookapp.request;

public class GoogleLoginRequest extends RegisterRequest{
    private String img;

    public GoogleLoginRequest(String email, String password, String name, String ip, String img) {
        super(email, password, name, ip);
        this.img = img;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
