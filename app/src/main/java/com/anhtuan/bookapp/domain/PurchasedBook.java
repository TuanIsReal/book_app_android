package com.anhtuan.bookapp.domain;

public class PurchasedBook {
    private String id;

    private String bookId;

    private String userId;

    private String bookName;

    private int lastReadChapter;

    private long lastReadTime;

    private int paymentPoint;

    private long purchasedTime;

    private boolean showLibrary;

    public PurchasedBook() {
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public int getLastReadChapter() {
        return lastReadChapter;
    }

    public void setLastReadChapter(int lastReadChapter) {
        this.lastReadChapter = lastReadChapter;
    }

    public long getLastReadTime() {
        return lastReadTime;
    }

    public void setLastReadTime(long lastReadTime) {
        this.lastReadTime = lastReadTime;
    }

    public int getPaymentPoint() {
        return paymentPoint;
    }

    public void setPaymentPoint(int paymentPoint) {
        this.paymentPoint = paymentPoint;
    }

    public long getPurchasedTime() {
        return purchasedTime;
    }

    public void setPurchasedTime(long purchasedTime) {
        this.purchasedTime = purchasedTime;
    }

    public boolean isShowLibrary() {
        return showLibrary;
    }

    public void setShowLibrary(boolean showLibrary) {
        this.showLibrary = showLibrary;
    }
}
