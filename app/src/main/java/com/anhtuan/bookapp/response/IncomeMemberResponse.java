package com.anhtuan.bookapp.response;

public class IncomeMemberResponse extends BaseResponse{
    private IncomeMemberData data;

    public IncomeMemberResponse(int code) {
        super(code);
    }

    public IncomeMemberResponse(int code, IncomeMemberData data) {
        super(code);
        this.data = data;
    }

    public IncomeMemberData getData() {
        return data;
    }

    public void setData(IncomeMemberData data) {
        this.data = data;
    }
}
