package com.anhtuan.bookapp.response;

import com.google.gson.annotations.SerializedName;

public class RegisterData {

    @SerializedName("userId")
    private String userId;

    @SerializedName("role")
    private int role;

    public RegisterData() {
    }

    public RegisterData(String userId, int role) {
        this.userId = userId;
        this.role = role;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }
}
