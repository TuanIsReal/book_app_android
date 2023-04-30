package com.anhtuan.bookapp.response;

import com.anhtuan.bookapp.domain.Category;

import java.util.ArrayList;

public class CategoriesResponse {
    private int code;
    private ArrayList<Category> data;

    public CategoriesResponse() {
    }

    public CategoriesResponse(int code, ArrayList<Category> data) {
        this.code = code;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public ArrayList<Category> getData() {
        return data;
    }

    public void setData(ArrayList<Category> data) {
        this.data = data;
    }
}
