package com.example.ragstorage.service;

import com.example.ragstorage.entity.ChatMessage;
import com.example.ragstorage.entity.ChatSession;
import com.example.ragstorage.repository.ChatMessageRepository;
import com.example.ragstorage.repository.ChatSessionRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class ChatMessageService {
    private final ChatMessageRepository msgRepo;
    private final ChatSessionRepository sessionRepo;

    public ChatMessageService(ChatMessageRepository msgRepo, ChatSessionRepository sessionRepo) {
        this.msgRepo = msgRepo;
        this.sessionRepo = sessionRepo;
    }

    @Transactional
    public ChatMessage addMessage(UUID sessionId, String sender, String content, String context) {
        ChatSession session = sessionRepo.findById(sessionId)
            .orElseThrow(() -> new RuntimeException("Session not found"));
        ChatMessage m = new ChatMessage(UUID.randomUUID(), session, sender, content, context);
        session.getMessages().add(m);
        sessionRepo.save(session);
        return m;
    }

    public Page<ChatMessage> getMessages(UUID sessionId, int page, int size) {
        return msgRepo.findBySessionIdOrderByCreatedAtAsc(sessionId, PageRequest.of(page, size));
    }
}
