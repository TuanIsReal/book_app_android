package com.anhtuan.bookapp.response;

import com.anhtuan.bookapp.domain.BookChapter;

import java.util.List;

public class GetBookChapterListResponse extends BaseResponse{
    private List<BookChapter> data;

    public GetBookChapterListResponse(int code) {
        super(code);
    }

    public GetBookChapterListResponse(int code, List<BookChapter> data) {
        super(code);
        this.data = data;
    }

    public List<BookChapter> getData() {
        return data;
    }

    public void setData(List<BookChapter> data) {
        this.data = data;
    }
}
