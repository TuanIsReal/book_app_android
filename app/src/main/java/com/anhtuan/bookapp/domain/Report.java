package com.anhtuan.bookapp.domain;

import java.io.Serializable;

public class Report implements Serializable {
    private String id;
    private String userId;
    private String bookId;
    private Integer type;
    private String content;
    private Integer status;
    private Long reportTime;
    private Long checkTime;

    public Report() {
    }

    public Report(String id, String userId, String bookId, Integer type, String content, Integer status, Long reportTime, Long checkTime) {
        this.id = id;
        this.userId = userId;
        this.bookId = bookId;
        this.type = type;
        this.content = content;
        this.status = status;
        this.reportTime = reportTime;
        this.checkTime = checkTime;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getReportTime() {
        return reportTime;
    }

    public void setReportTime(Long reportTime) {
        this.reportTime = reportTime;
    }

    public Long getCheckTime() {
        return checkTime;
    }

    public void setCheckTime(Long checkTime) {
        this.checkTime = checkTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
