package com.anhtuan.bookapp.response;

import com.anhtuan.bookapp.domain.Payment;

import java.util.List;

public class IncomeAdminData {
    private int totalPoint;
    private long totalMoney;
    private int transactionPoint;
    List<Payment> paymentList;

    public IncomeAdminData() {
    }

    public int getTotalPoint() {
        return totalPoint;
    }

    public void setTotalPoint(int totalPoint) {
        this.totalPoint = totalPoint;
    }

    public long getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(long totalMoney) {
        this.totalMoney = totalMoney;
    }

    public int getTransactionPoint() {
        return transactionPoint;
    }

    public void setTransactionPoint(int transactionPoint) {
        this.transactionPoint = transactionPoint;
    }

    public List<Payment> getPaymentList() {
        return paymentList;
    }

    public void setPaymentList(List<Payment> paymentList) {
        this.paymentList = paymentList;
    }
}
