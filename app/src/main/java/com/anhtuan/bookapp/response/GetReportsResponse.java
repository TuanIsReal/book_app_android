package com.anhtuan.bookapp.response;

import com.anhtuan.bookapp.domain.Report;

import java.util.List;

public class GetReportsResponse extends BaseResponse{
    List<Report> data;

    public GetReportsResponse(int code) {
        super(code);
    }

    public GetReportsResponse(int code, List<Report> data) {
        super(code);
        this.data = data;
    }

    public List<Report> getData() {
        return data;
    }

    public void setData(List<Report> data) {
        this.data = data;
    }
}
