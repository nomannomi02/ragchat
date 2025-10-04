package com.example.ragstorage.controller;

import com.example.ragstorage.dto.CreateMessageRequest;
import com.example.ragstorage.entity.ChatMessage;
import com.example.ragstorage.service.ChatMessageService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/sessions/{sessionId}/messages")
public class ChatMessageController {
    private final ChatMessageService service;

    public ChatMessageController(ChatMessageService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ChatMessage> add(@PathVariable UUID sessionId, @RequestBody CreateMessageRequest req) {
        ChatMessage m = service.addMessage(sessionId, req.getSender(), req.getContent(), req.getContext());
        return ResponseEntity.ok(m);
    }

    @GetMapping
    public ResponseEntity<Page<ChatMessage>> list(@PathVariable UUID sessionId,
                                                  @RequestParam(defaultValue = "0") int page,
                                                  @RequestParam(defaultValue = "20") int size) {
        Page<ChatMessage> p = service.getMessages(sessionId, page, size);
        return ResponseEntity.ok(p);
    }
}
