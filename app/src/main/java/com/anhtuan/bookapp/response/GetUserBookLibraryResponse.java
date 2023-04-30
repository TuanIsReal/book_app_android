package com.anhtuan.bookapp.response;

import com.anhtuan.bookapp.domain.UserBookLibrary;

import java.util.List;

public class GetUserBookLibraryResponse extends BaseResponse{
    private List<UserBookLibrary> data;

    public GetUserBookLibraryResponse(int code, List<UserBookLibrary> data) {
        super(code);
        this.data = data;
    }

    public GetUserBookLibraryResponse(int code) {
        super(code);
    }

    public List<UserBookLibrary> getData() {
        return data;
    }

    public void setData(List<UserBookLibrary> data) {
        this.data = data;
    }
}
