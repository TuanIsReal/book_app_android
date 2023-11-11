package com.anhtuan.bookapp.response;

import com.anhtuan.bookapp.domain.BookReview;

import java.util.List;

public class GetBookReviewResponse extends BaseResponse{
    private BookReview data;

    public GetBookReviewResponse(int code) {
        super(code);
    }

    public GetBookReviewResponse(int code, BookReview data) {
        super(code);
        this.data = data;
    }

    public BookReview getData() {
        return data;
    }

    public void setData(BookReview data) {
        this.data = data;
    }
}
