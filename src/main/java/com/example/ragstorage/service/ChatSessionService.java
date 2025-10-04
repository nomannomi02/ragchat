package com.example.ragstorage.service;

import com.example.ragstorage.entity.ChatSession;
import com.example.ragstorage.exception.ResourceNotFoundException;
import com.example.ragstorage.repository.ChatSessionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.module.ResolutionException;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ChatSessionService {
    private final ChatSessionRepository repo;

    public ChatSessionService(ChatSessionRepository repo) {
        this.repo = repo;
    }

    @Transactional
    public ChatSession createSession(String userId, String title) {
        ChatSession s = new ChatSession(UUID.randomUUID(), userId, title);
        return repo.save(s);
    }

    @Transactional
    public ChatSession rename(UUID sessionId, String title) {
        ChatSession s = repo.findById(sessionId).orElseThrow(() -> new ResourceNotFoundException("Session not found"));
        s.setTitle(title);
        s.setUpdatedAt(Instant.now());
        return repo.save(s);
    }

    @Transactional
    public ChatSession setFavorite(UUID sessionId, boolean fav) {
        ChatSession s = repo.findById(sessionId).orElseThrow(() -> new ResourceNotFoundException("Session not found"));
        s.setFavorite(fav);
        s.setUpdatedAt(Instant.now());
        return repo.save(s);
    }

    @Transactional
    public void delete(UUID sessionId) {
        repo.deleteById(sessionId);
    }

    public Optional<ChatSession> get(UUID id) {
        return repo.findById(id);
    }

    public List<ChatSession> getAllSessions() {
        return repo.findAll();
    }
}
