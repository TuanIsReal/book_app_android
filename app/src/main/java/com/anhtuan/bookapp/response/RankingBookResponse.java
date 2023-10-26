package com.anhtuan.bookapp.response;

public class RankingBookResponse extends BaseResponse{
    private Object data;

    public RankingBookResponse(int code) {
        super(code);
    }

    public RankingBookResponse(int code, Object data) {
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
