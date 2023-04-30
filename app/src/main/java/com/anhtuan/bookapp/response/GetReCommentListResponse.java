package com.anhtuan.bookapp.response;

import com.anhtuan.bookapp.domain.ReComment;

import java.util.List;

public class GetReCommentListResponse extends BaseResponse{
    private List<ReComment> data;

    public GetReCommentListResponse(int code) {
        super(code);
    }

    public GetReCommentListResponse(int code, List<ReComment> data) {
        super(code);
        this.data = data;
    }

    public List<ReComment> getData() {
        return data;
    }

    public void setData(List<ReComment> data) {
        this.data = data;
    }
}
