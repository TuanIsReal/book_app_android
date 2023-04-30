package com.anhtuan.bookapp.response;

public class GetBookImageResponse extends BaseResponse{
    private byte[] data;

    public GetBookImageResponse(int code) {
        super(code);
    }

    public GetBookImageResponse(int code, byte[] data) {
        super(code);
        this.data = data;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
}
