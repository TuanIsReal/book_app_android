package com.anhtuan.bookapp.response;

import com.anhtuan.bookapp.domain.User;

import java.util.List;

public class SearchUserResponse extends BaseResponse{
    private List<User> data;

    public SearchUserResponse(int code, List<User> data) {
        super(code);
        this.data = data;
    }

    public SearchUserResponse(int code) {
        super(code);
    }

    public List<User> getData() {
        return data;
    }

    public void setData(List<User> data) {
        this.data = data;
    }
}
