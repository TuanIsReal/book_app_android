package com.anhtuan.bookapp.domain;

public class ReComment {
    private String id;

    private String parentCommentId;

    private String author;

    private String commentContent;

    private long commentTime;

    public ReComment() {
    }

    public ReComment(String id, String parentCommentId, String author, String commentContent, long commentTime) {
        this.id = id;
        this.parentCommentId = parentCommentId;
        this.author = author;
        this.commentContent = commentContent;
        this.commentTime = commentTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public long getCommentTime() {
        return commentTime;
    }

    public void setCommentTime(long commentTime) {
        this.commentTime = commentTime;
    }
}
