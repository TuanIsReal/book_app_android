package com.anhtuan.bookapp.response;

import com.anhtuan.bookapp.domain.BookChapter;

public class GetChapterContentResponse extends BaseResponse{
    private BookChapter data;

    public GetChapterContentResponse(int code, BookChapter data) {
        super(code);
        this.data = data;
    }

    public GetChapterContentResponse(int code) {
        super(code);
    }

    public BookChapter getData() {
        return data;
    }

    public void setData(BookChapter data) {
        this.data = data;
    }
}
