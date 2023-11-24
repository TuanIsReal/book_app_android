package com.anhtuan.bookapp.response;

public class GetUserDetailResponse extends BaseResponse{
    private GetUserDetailData data;

    public GetUserDetailResponse(int code) {
        super(code);
    }

    public GetUserDetailResponse(int code, GetUserDetailData data) {
        super(code);
        this.data = data;
    }

    public GetUserDetailData getData() {
        return data;
    }

    public void setData(GetUserDetailData data) {
        this.data = data;
    }
}
