package com.anhtuan.bookapp.request;

public class AddReportRequest {
    private String bookId;
    private int type;
    private String content;

    public AddReportRequest(String bookId, int type, String content) {
        this.bookId = bookId;
        this.type = type;
        this.content = content;
    }

    public AddReportRequest() {
    }
}
