package com.anhtuan.bookapp.response;

import com.anhtuan.bookapp.domain.BookRequestUp;

import java.util.List;

public class GetRequestUploadBookResponse extends BaseResponse{
    private List<BookRequestUp> data;

    public GetRequestUploadBookResponse(int code) {
        super(code);
    }

    public GetRequestUploadBookResponse(int code, List<BookRequestUp> data) {
        super(code);
        this.data = data;
    }

    public List<BookRequestUp> getData() {
        return data;
    }

    public void setData(List<BookRequestUp> data) {
        this.data = data;
    }
}
