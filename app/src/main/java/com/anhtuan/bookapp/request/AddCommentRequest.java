package com.anhtuan.bookapp.request;

public class AddCommentRequest {
    private String bookId;
    private String author;
    private String commentContent;

    public AddCommentRequest() {
    }

    public AddCommentRequest(String bookId, String author, String commentContent) {
        this.bookId = bookId;
        this.author = author;
        this.commentContent = commentContent;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }
}
