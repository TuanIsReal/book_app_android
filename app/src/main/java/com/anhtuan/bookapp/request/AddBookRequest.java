package com.anhtuan.bookapp.request;

import java.util.ArrayList;
import java.util.List;

public class AddBookRequest {
    private String userPost;
    private String bookName;
    private String author;
    private String introduction;
    private String bookImage;
    private ArrayList<String> bookCategory;
    private int bookPrice;

    public AddBookRequest() {
    }

    public AddBookRequest(String userPost, String bookName, String author, String introduction, String bookImage, ArrayList<String> bookCategory, int bookPrice) {
        this.userPost = userPost;
        this.bookName = bookName;
        this.author = author;
        this.introduction = introduction;
        this.bookImage = bookImage;
        this.bookCategory = bookCategory;
        this.bookPrice = bookPrice;
    }
}
