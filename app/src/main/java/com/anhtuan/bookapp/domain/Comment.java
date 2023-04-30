package com.anhtuan.bookapp.domain;

import java.io.Serializable;

public class Comment implements Serializable {
    private String id;

    private String bookId;

    private String author;

    private String commentContent;

    private int totalReComment;

    private long commentTime;

    public Comment() {
    }

    public Comment(String id, String bookId, String author, String commentContent, int totalReComment, long commentTime) {
        this.id = id;
        this.bookId = bookId;
        this.author = author;
        this.commentContent = commentContent;
        this.totalReComment = totalReComment;
        this.commentTime = commentTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public int getTotalReComment() {
        return totalReComment;
    }

    public void setTotalReComment(int totalReComment) {
        this.totalReComment = totalReComment;
    }

    public long getCommentTime() {
        return commentTime;
    }

    public void setCommentTime(long commentTime) {
        this.commentTime = commentTime;
    }
}
