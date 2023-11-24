package com.anhtuan.bookapp.response;

public class GetUserDetailData {
    private int totalBook;
    private int totalPurchasedBook;
    private double purchasedPoint;
    private double earnedPoint;

    public GetUserDetailData(int totalBook, int totalPurchasedBook, double purchasedPoint, double earnedPoint) {
        this.totalBook = totalBook;
        this.totalPurchasedBook = totalPurchasedBook;
        this.purchasedPoint = purchasedPoint;
        this.earnedPoint = earnedPoint;
    }

    public GetUserDetailData() {
    }

    public int getTotalBook() {
        return totalBook;
    }

    public void setTotalBook(int totalBook) {
        this.totalBook = totalBook;
    }

    public int getTotalPurchasedBook() {
        return totalPurchasedBook;
    }

    public void setTotalPurchasedBook(int totalPurchasedBook) {
        this.totalPurchasedBook = totalPurchasedBook;
    }

    public double getPurchasedPoint() {
        return purchasedPoint;
    }

    public void setPurchasedPoint(double purchasedPoint) {
        this.purchasedPoint = purchasedPoint;
    }

    public double getEarnedPoint() {
        return earnedPoint;
    }

    public void setEarnedPoint(double earnedPoint) {
        this.earnedPoint = earnedPoint;
    }
}
