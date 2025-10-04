package com.example.ragstorage.dto;

public class CreateMessageRequest {
    private String sender;
    private String content;
    private String context;

    public CreateMessageRequest() {}

    public String getSender() { return sender; }
    public void setSender(String sender) { this.sender = sender; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getContext() { return context; }
    public void setContext(String context) { this.context = context; }
}
