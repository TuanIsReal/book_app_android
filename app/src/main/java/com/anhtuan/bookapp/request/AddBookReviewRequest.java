package com.anhtuan.bookapp.request;

public class AddBookReviewRequest {
    private String bookId;
    private double reviewStar;
    private String reviewContent;

    public AddBookReviewRequest() {
    }

    public AddBookReviewRequest(String bookId, double reviewStar, String reviewContent) {
        this.bookId = bookId;
        this.reviewStar = reviewStar;
        this.reviewContent = reviewContent;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public double getReviewStar() {
        return reviewStar;
    }

    public void setReviewStar(int reviewStar) {
        this.reviewStar = reviewStar;
    }

    public String getReviewContent() {
        return reviewContent;
    }

    public void setReviewContent(String reviewContent) {
        this.reviewContent = reviewContent;
    }

}
