package com.anhtuan.bookapp.request;

import java.util.ArrayList;

public class UpdateBookRequest {
    private String bookId;
    private String introduction;
    private ArrayList<String> bookCategory;
    private int bookPrice;
    private int freeChapter;
    private Boolean isFinish;

    public UpdateBookRequest(String bookId, String introduction, ArrayList<String> bookCategory, int bookPrice, int freeChapter, Boolean isFinish) {
        this.bookId = bookId;
        this.introduction = introduction;
        this.bookCategory = bookCategory;
        this.bookPrice = bookPrice;
        this.freeChapter = freeChapter;
        this.isFinish = isFinish;
    }
}
