package com.anhtuan.bookapp.domain;

public class UserBookLibrary {
    private String bookId;
    private String bookName;
    private int lastRead;
    private int totalChapter;
    private String bookImage;

    public UserBookLibrary() {
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public int getLastRead() {
        return lastRead;
    }

    public void setLastRead(int lastRead) {
        this.lastRead = lastRead;
    }

    public int getTotalChapter() {
        return totalChapter;
    }

    public void setTotalChapter(int totalChapter) {
        this.totalChapter = totalChapter;
    }

    public String getBookImage() {
        return bookImage;
    }

    public void setBookImage(String bookImage) {
        this.bookImage = bookImage;
    }

    @Override
    public String toString() {
        return "UserBookLibrary{" +
                "bookId='" + bookId + '\'' +
                ", bookName='" + bookName + '\'' +
                ", lastRead=" + lastRead +
                ", totalChapter=" + totalChapter +
                ", bookImage='" + bookImage + '\'' +
                '}';
    }
}
