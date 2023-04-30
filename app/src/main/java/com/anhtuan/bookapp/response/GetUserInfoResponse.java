package com.anhtuan.bookapp.response;

import com.anhtuan.bookapp.domain.User;

public class GetUserInfoResponse extends BaseResponse{
    private User data;

    public GetUserInfoResponse(User data) {
        this.data = data;
    }

    public GetUserInfoResponse(int code, User data) {
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
