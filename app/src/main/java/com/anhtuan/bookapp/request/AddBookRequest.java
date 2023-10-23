package com.anhtuan.bookapp.request;

import java.util.ArrayList;
import java.util.List;

public class AddBookRequest {
    private String bookName;
    private String author;
    private String introduction;
    private String bookImage;
    private ArrayList<String> bookCategory;
    private int bookPrice;
    private int freeChapter;

    public AddBookRequest() {
    }

    public AddBookRequest(String bookName, String author, String introduction, String bookImage, ArrayList<String> bookCategory, int bookPrice, int freeChapter) {
        this.bookName = bookName;
        this.author = author;
        this.introduction = introduction;
        this.bookImage = bookImage;
        this.bookCategory = bookCategory;
        this.bookPrice = bookPrice;
        this.freeChapter = freeChapter;
    }
}
