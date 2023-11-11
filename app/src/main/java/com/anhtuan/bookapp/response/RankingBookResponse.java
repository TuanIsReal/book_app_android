package com.anhtuan.bookapp.response;

import java.util.Map;

public class RankingBookResponse extends BaseResponse{
    private Map<String, Double> data;

    public RankingBookResponse(int code) {
        super(code);
    }

    public RankingBookResponse(int code, Map<String, Double> data) {
        super(code);
        this.data = data;
    }

    public Map<String, Double> getData() {
        return data;
    }

    public void setData(Map<String, Double> data) {
        this.data = data;
    }
}
