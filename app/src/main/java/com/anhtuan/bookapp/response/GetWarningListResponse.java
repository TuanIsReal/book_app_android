package com.anhtuan.bookapp.response;

import com.anhtuan.bookapp.domain.WarningChapter;

import java.util.List;

public class GetWarningListResponse extends BaseResponse{
    private List<WarningChapter> data;

    public GetWarningListResponse(int code) {
        super(code);
    }

    public GetWarningListResponse(int code, List<WarningChapter> data) {
        super(code);
        this.data = data;
    }

    public List<WarningChapter> getData() {
        return data;
    }

    public void setData(List<WarningChapter> data) {
        this.data = data;
    }
}
