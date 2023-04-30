package com.anhtuan.bookapp.response;

import com.anhtuan.bookapp.domain.PurchasedBook;

public class GetPurchasedBookResponse extends BaseResponse{
    private PurchasedBook data;

    public GetPurchasedBookResponse(int code) {
        super(code);
    }

    public GetPurchasedBookResponse(int code, PurchasedBook data) {
        super(code);
        this.data = data;
    }

    public PurchasedBook getData() {
        return data;
    }

    public void setData(PurchasedBook data) {
        this.data = data;
    }
}
