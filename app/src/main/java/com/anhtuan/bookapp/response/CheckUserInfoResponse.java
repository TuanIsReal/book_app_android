package com.anhtuan.bookapp.response;

import com.anhtuan.bookapp.domain.User;

public class CheckUserInfoResponse extends BaseResponse{
    private User data;

    public CheckUserInfoResponse(User data) {
        this.data = data;
    }

    public CheckUserInfoResponse(int code, User data) {
        super(code);
        this.data = data;
    }

    public User getData() {
        return data;
    }

    public void setData(User data) {
        this.data = data;
    }
}
