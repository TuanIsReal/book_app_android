package com.anhtuan.bookapp.response;

import com.anhtuan.bookapp.domain.Book;

public class ViewBookResponse extends BaseResponse{

    private Book data;

    public ViewBookResponse(int code) {
        super(code);
    }

    public ViewBookResponse(int code, Book data) {
        super(code);
        this.data = data;
    }

    public Book getData() {
        return data;
    }

    public void setData(Book data) {
        this.data = data;
    }
}
