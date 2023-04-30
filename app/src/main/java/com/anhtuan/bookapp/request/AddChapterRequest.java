package com.anhtuan.bookapp.request;

public class AddChapterRequest {
    private String bookName;
    private int chapterNumber;
    private String chapterName;
    private String chapterContent;

    public AddChapterRequest() {
    }

    public AddChapterRequest(String bookName, int chapterNumber, String chapterName, String chapterContent) {
        this.bookName = bookName;
        this.chapterNumber = chapterNumber;
        this.chapterName = chapterName;
        this.chapterContent = chapterContent;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
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
}
