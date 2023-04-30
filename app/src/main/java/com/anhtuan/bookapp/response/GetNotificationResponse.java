package com.anhtuan.bookapp.response;

import com.anhtuan.bookapp.domain.Notification;

import java.util.List;

public class GetNotificationResponse extends BaseResponse{
    private List<Notification> data;

    public GetNotificationResponse(int code) {
        super(code);
    }

    public GetNotificationResponse(int code, List<Notification> data) {
        super(code);
        this.data = data;
    }

    public List<Notification> getData() {
        return data;
    }

    public void setData(List<Notification> data) {
        this.data = data;
    }
}
