package com.anhtuan.bookapp.request;

public class AddReCommentRequest {
    private String parentCommentId;
    private String commentContent;

    public AddReCommentRequest() {
    }

    public AddReCommentRequest(String parentCommentId, String commentContent) {
        this.parentCommentId = parentCommentId;
        this.commentContent = commentContent;
    }

    public String getParentCommentId() {
        return parentCommentId;
    }

    public void setParentCommentId(String parentCommentId) {
        this.parentCommentId = parentCommentId;
    }


    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }
}
