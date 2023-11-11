package com.anhtuan.bookapp.response;

import com.anhtuan.bookapp.domain.BookReview;

import java.util.List;

public class GetBookReviewsResponse extends BaseResponse{

    private List<BookReview> data;

    public GetBookReviewsResponse(int code) {
        super(code);
    }

    public GetBookReviewsResponse(int code, List<BookReview> data) {
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
