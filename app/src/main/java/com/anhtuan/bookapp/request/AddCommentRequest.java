package com.anhtuan.bookapp.request;

public class AddCommentRequest {
    private String bookId;
    private String commentContent;

    public AddCommentRequest() {
    }

    public AddCommentRequest(String bookId, String commentContent) {
        this.bookId = bookId;
        this.commentContent = commentContent;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }
}
