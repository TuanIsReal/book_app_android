package com.anhtuan.bookapp.response;

import com.anhtuan.bookapp.domain.TransactionHistory;

import java.util.List;

public class IncomeMemberData {
    private int revenue;
    private int spend;
    private int change;
    private List<TransactionHistory> transactionHistoryList;

    public IncomeMemberData() {
    }

    public int getRevenue() {
        return revenue;
    }

    public void setRevenue(int revenue) {
        this.revenue = revenue;
    }

    public int getSpend() {
        return spend;
    }

    public void setSpend(int spend) {
        this.spend = spend;
    }

    public int getChange() {
        return change;
    }

    public void setChange(int change) {
        this.change = change;
    }

    public List<TransactionHistory> getTransactionHistoryList() {
        return transactionHistoryList;
    }

    public void setTransactionHistoryList(List<TransactionHistory> transactionHistoryList) {
        this.transactionHistoryList = transactionHistoryList;
    }
}
