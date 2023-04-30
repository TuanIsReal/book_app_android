package com.anhtuan.bookapp.response;

import com.anhtuan.bookapp.domain.Comment;

import java.util.List;

public class GetCommentListResponse extends BaseResponse{
    private List<Comment> data;

    public GetCommentListResponse(int code) {
        super(code);
    }

    public GetCommentListResponse(int code, List<Comment> data) {
        super(code);
        this.data = data;
    }

    public List<Comment> getData() {
        return data;
    }

    public void setData(List<Comment> data) {
        this.data = data;
    }
}
