package com.anhtuan.bookapp.response;


public class IncomeAdminResponse extends BaseResponse {
    private IncomeAdminData data;

    public IncomeAdminResponse(int code) {
        super(code);
    }

    public IncomeAdminResponse(int code, IncomeAdminData data) {
        super(code);
        this.data = data;
    }

    public IncomeAdminData getData() {
        return data;
    }

    public void setData(IncomeAdminData data) {
        this.data = data;
    }
}
