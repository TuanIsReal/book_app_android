package com.anhtuan.bookapp.response;

import com.anhtuan.bookapp.domain.Book;

import java.util.ArrayList;

public class SearchBookResponse extends BaseResponse{
    private ArrayList<Book> data;

    public SearchBookResponse(int code) {
        super(code);
    }

    public SearchBookResponse(int code, ArrayList<Book> data) {
        super(code);
        this.data = data;
    }

    public ArrayList<Book> getData() {
        return data;
    }

    public void setData(ArrayList<Book> data) {
        this.data = data;
    }
}
