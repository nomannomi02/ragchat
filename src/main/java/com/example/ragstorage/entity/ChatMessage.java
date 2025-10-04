package com.example.ragstorage.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "chat_message")
public class ChatMessage {
    @Id
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id", nullable = false)
    @JsonBackReference
    private ChatSession session;

    private String sender;

    @Column(columnDefinition = "text")
    private String content;

    @Column(columnDefinition = "text")
    private String context;

    @Column(name = "created_at", updatable = false)
    private Instant createdAt = Instant.now();

    public ChatMessage() {}

    public ChatMessage(UUID id, ChatSession session, String sender, String content, String context) {
        this.id = id;
        this.session = session;
        this.sender = sender;
        this.content = content;
        this.context = context;
    }

    // getters and setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public ChatSession getSession() { return session; }
    public void setSession(ChatSession session) { this.session = session; }

    public String getSender() { return sender; }
    public void setSender(String sender) { this.sender = sender; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getContext() { return context; }
    public void setContext(String context) { this.context = context; }

    public Instant getCreatedAt() { return createdAt; }
}
