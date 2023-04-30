package com.anhtuan.bookapp.request;

public class AddReCommentRequest {
    private String parentCommentId;
    private String author;
    private String commentContent;

    public AddReCommentRequest() {
    }

    public AddReCommentRequest(String parentCommentId, String author, String commentContent) {
        this.parentCommentId = parentCommentId;
        this.author = author;
        this.commentContent = commentContent;
    }

    public String getParentCommentId() {
        return parentCommentId;
    }

    public void setParentCommentId(String parentCommentId) {
        this.parentCommentId = parentCommentId;
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
