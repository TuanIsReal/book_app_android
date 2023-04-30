package com.anhtuan.bookapp.response;

import com.anhtuan.bookapp.domain.BookReview;

import java.util.List;

public class GetBookReviewResponse extends BaseResponse{
    private List<BookReview> data;

    public GetBookReviewResponse(int code) {
        super(code);
    }

    public GetBookReviewResponse(int code, List<BookReview> data) {
        super(code);
        this.data = data;
    }

    public List<BookReview> getData() {
        return data;
    }

    public void setData(List<BookReview> data) {
        this.data = data;
    }
}
