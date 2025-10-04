package com.example.ragstorage.dto;

public class CreateSessionRequest {
    private String userId;
    private String title;

    public CreateSessionRequest() {}
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
}
