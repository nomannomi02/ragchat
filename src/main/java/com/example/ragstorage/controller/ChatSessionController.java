package com.example.ragstorage.controller;

import com.example.ragstorage.dto.CreateSessionRequest;
import com.example.ragstorage.entity.ChatSession;
import com.example.ragstorage.service.ChatSessionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/sessions")
public class ChatSessionController {
    private final ChatSessionService service;

    public ChatSessionController(ChatSessionService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ChatSession> create(@RequestBody CreateSessionRequest req) {
        ChatSession s = service.createSession(req.getUserId(), req.getTitle());
        return ResponseEntity.ok(s);
    }

    @PatchMapping("/{id}/rename")
    public ResponseEntity<ChatSession> rename(@PathVariable UUID id, @RequestBody CreateSessionRequest req) {
        ChatSession s = service.rename(id, req.getTitle());
        return ResponseEntity.ok(s);
    }

    @PatchMapping("/{id}/favorite")
    public ResponseEntity<ChatSession> favorite(@PathVariable UUID id, @RequestParam boolean favorite) {
        ChatSession s = service.setFavorite(id, favorite);
        return ResponseEntity.ok(s);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ChatSession> get(@PathVariable UUID id) {
        return service.get(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<ChatSession>> getAllSessions() {
        List<ChatSession> sessions = service.getAllSessions();
        return ResponseEntity.ok(sessions);
    }
}
