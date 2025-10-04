package com.example.ragstorage;

import com.example.ragstorage.entity.ChatSession;
import com.example.ragstorage.repository.ChatSessionRepository;
import com.example.ragstorage.service.ChatSessionService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

public class ChatSessionServiceTest {

    @Test
    void createSession() {
        ChatSessionRepository repo = Mockito.mock(ChatSessionRepository.class);
        Mockito.when(repo.save(any())).thenAnswer(i -> i.getArgument(0));
        ChatSessionService service = new ChatSessionService(repo);
        ChatSession s = service.createSession("user1", "title1");
        assertEquals("user1", s.getUserId());
        assertEquals("title1", s.getTitle());
        assertNotNull(s.getId());
    }
}
