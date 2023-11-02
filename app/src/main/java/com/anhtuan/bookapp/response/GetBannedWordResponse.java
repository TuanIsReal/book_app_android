package com.anhtuan.bookapp.response;

import com.anhtuan.bookapp.domain.BannedWord;


public class GetBannedWordResponse extends BaseResponse{
    private BannedWord data;

    public GetBannedWordResponse(int code) {
        super(code);
    }

    public GetBannedWordResponse(int code, BannedWord data) {
        super(code);
        this.data = data;
    }

    public BannedWord getData() {
        return data;
    }
}
