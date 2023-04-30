package com.anhtuan.bookapp.response;

import com.anhtuan.bookapp.domain.Book;

import java.util.List;

public class GetBookResponse extends BaseResponse{
    private List<Book> data;


    public GetBookResponse(List<Book> data) {
        this.data = data;
    }

    public GetBookResponse(int code, List<Book> data) {
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
