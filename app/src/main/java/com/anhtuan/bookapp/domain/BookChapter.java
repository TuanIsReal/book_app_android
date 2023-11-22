package com.anhtuan.bookapp.domain;

import java.io.Serializable;

public class BookChapter implements Serializable {
    private String id;

    private String bookId;

    private int chapterNumber;

    private String chapterName;

    private String chapterContent;

    private long uploadTime;

    private long lastUpdateTime;

    public BookChapter() {
    }

    public BookChapter(String id, String bookId, int chapterNumber, String chapterName, String chapterContent, long uploadTime, long lastUpdateTime) {
        this.id = id;
        this.bookId = bookId;
        this.chapterNumber = chapterNumber;
        this.chapterName = chapterName;
        this.chapterContent = chapterContent;
        this.uploadTime = uploadTime;
        this.lastUpdateTime = lastUpdateTime;
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

    public int getChapterNumber() {
        return chapterNumber;
    }

    public void setChapterNumber(int chapterNumber) {
        this.chapterNumber = chapterNumber;
    }

    public String getChapterName() {
        return chapterName;
    }

    public void setChapterName(String chapterName) {
        this.chapterName = chapterName;
    }

    public String getChapterContent() {
        return chapterContent;
    }

    public void setChapterContent(String chapterContent) {
        this.chapterContent = chapterContent;
    }

    public long getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(long uploadTime) {
        this.uploadTime = uploadTime;
    }

    public long getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(long lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }
}
