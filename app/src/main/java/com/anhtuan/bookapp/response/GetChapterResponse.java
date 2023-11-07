package com.anhtuan.bookapp.response;

import com.anhtuan.bookapp.domain.BookChapter;

public class GetChapterResponse extends BaseResponse{
    BookChapter data;

    public GetChapterResponse(int code, BookChapter data){
        super(code);
        this.data = data;
    }

    public GetChapterResponse(int code) {
        super(code);
    }

    public BookChapter getData() {
        return data;
    }

    public void setData(BookChapter data) {
        this.data = data;
    }
}
