package com.anhtuan.bookapp.response;

public class RankingUserResponse extends BaseResponse{
    private Object data;

    public RankingUserResponse(int code) {
        super(code);
    }

    public RankingUserResponse(int code, Object data) {
        super(code);
        this.data = data;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
