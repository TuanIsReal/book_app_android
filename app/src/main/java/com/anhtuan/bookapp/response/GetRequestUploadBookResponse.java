package com.anhtuan.bookapp.response;

import com.anhtuan.bookapp.domain.Book;
import com.anhtuan.bookapp.domain.BookRequestUp;

import java.util.List;

public class GetRequestUploadBookResponse extends BaseResponse{
    private List<Book> data;

    public GetRequestUploadBookResponse(int code) {
        super(code);
    }

    public GetRequestUploadBookResponse(int code, List<Book> data) {
        super(code);
        this.data = data;
    }

    public List<Book> getData() {
        return data;
    }

    public void setData(List<Book> data) {
        this.data = data;
    }
}
