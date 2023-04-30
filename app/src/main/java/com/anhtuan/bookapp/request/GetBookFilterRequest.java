package com.anhtuan.bookapp.request;

import java.util.List;

public class GetBookFilterRequest {
    int sort;
    int order;
    int status;
    int post;
    List<String> category;
    int page;

    public GetBookFilterRequest() {
    }

    public GetBookFilterRequest(int sort, int order, int status, int post, List<String> category, int page) {
        this.sort = sort;
        this.order = order;
        this.status = status;
        this.post = post;
        this.category = category;
        this.page = page;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getPost() {
        return post;
    }

    public void setPost(int post) {
        this.post = post;
    }

    public List<String> getCategory() {
        return category;
    }

    public void setCategory(List<String> category) {
        this.category = category;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }
}
