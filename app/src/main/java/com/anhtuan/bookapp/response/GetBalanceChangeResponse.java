package com.anhtuan.bookapp.response;

import com.anhtuan.bookapp.domain.TransactionHistory;

import java.util.List;

public class GetBalanceChangeResponse extends BaseResponse{
    private List<TransactionHistory> data;

    public GetBalanceChangeResponse(int code) {
        super(code);
    }

    public GetBalanceChangeResponse(int code, List<TransactionHistory> data) {
        super(code);
        this.data = data;
    }

    public List<TransactionHistory> getData() {
        return data;
    }

    public void setData(List<TransactionHistory> data) {
        this.data = data;
    }
}
