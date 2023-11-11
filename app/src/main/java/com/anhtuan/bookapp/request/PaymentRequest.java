package com.anhtuan.bookapp.request;

public class PaymentRequest {
    int point;
    String description;

    public PaymentRequest() {
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
