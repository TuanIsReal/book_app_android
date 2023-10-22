package com.anhtuan.bookapp.domain;

public class User {

    private String id;

    private String email;

    private String password;

    private String role;

    private String name;

    private String avatarImage;

    private String lastLoginIp;

    private Boolean isLogged;

    private int point;

    private Boolean isGoogleLogin;

    public User() {
    }

    public User(String id, String email, String password, String role, String name, String avatarImage, String lastLoginIp, Boolean isLogged, int point) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.role = role;
        this.name = name;
        this.avatarImage = avatarImage;
        this.lastLoginIp = lastLoginIp;
        this.isLogged = isLogged;
        this.point = point;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatarImage() {
        return avatarImage;
    }

    public void setAvatarImage(String avatarImage) {
        this.avatarImage = avatarImage;
    }

    public String getLastLoginIp() {
        return lastLoginIp;
    }

    public void setLastLoginIp(String lastLoginIp) {
        this.lastLoginIp = lastLoginIp;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public Boolean getLogged() {
        return isLogged;
    }

    public void setLogged(Boolean logged) {
        isLogged = logged;
    }

    public Boolean getGoogleLogin() {
        return isGoogleLogin;
    }

    public void setGoogleLogin(Boolean googleLogin) {
        isGoogleLogin = googleLogin;
    }
}
