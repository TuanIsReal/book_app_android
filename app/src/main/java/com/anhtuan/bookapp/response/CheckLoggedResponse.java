package com.anhtuan.bookapp.response;

import com.anhtuan.bookapp.domain.User;

public class CheckLoggedResponse extends BaseResponse{
    private User data;

    public CheckLoggedResponse() {
    }

    public CheckLoggedResponse(User data) {
        this.data = data;
    }

    public CheckLoggedResponse(int code, User data) {
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
