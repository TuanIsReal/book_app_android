package com.anhtuan.bookapp.request;

public class RegisterRequest {

    private String email;

    private String password;

    private String name;

    private String ip;

    public RegisterRequest() {
    }

    public RegisterRequest(String email, String password, String name, String ip) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.ip = ip;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setName(String name) {
        this.name = name;
    }


}
