package com.anhtuan.bookapp.response;

import com.anhtuan.bookapp.domain.WarningChapter;

import java.util.List;

public class GetWarningListResponse extends BaseResponse{
    private List<GetWarningListData> data;

    public GetWarningListResponse(int code) {
        super(code);
    }

    public GetWarningListResponse(int code, List<GetWarningListData> data) {
        super(code);
        this.data = data;
    }

    public List<GetWarningListData> getData() {
        return data;
    }

    public void setData(List<GetWarningListData> data) {
        this.data = data;
    }
}
