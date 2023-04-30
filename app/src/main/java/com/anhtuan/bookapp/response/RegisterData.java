package com.anhtuan.bookapp.response;

import com.google.gson.annotations.SerializedName;

public class RegisterData {

    @SerializedName("userId")
    private String userId;

    @SerializedName("role")
    private String role;

    public RegisterData() {
    }

    public RegisterData(String userId, String role) {
        this.userId = userId;
        this.role = role;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
