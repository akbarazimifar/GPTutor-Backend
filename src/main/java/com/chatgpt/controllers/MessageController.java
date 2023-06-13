package com.chatgpt.controllers;

import com.chatgpt.entity.CreateMessageRequest;
import com.chatgpt.entity.Message;
import com.chatgpt.services.MessageService;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@RestController
public class MessageController {
    @Autowired
    MessageService messageService;

    @PostMapping(path = "/messages")
    @RateLimiter(name = "messagesLimit", fallbackMethod = "fallbackMethod")
    public ResponseEntity<Message> createMessage(@RequestBody CreateMessageRequest createMessageRequest) throws Exception {
        return ResponseEntity.ok().body(messageService.createMessage(createMessageRequest));
    }

    @GetMapping(path = "/messages/{historyId}")
    @RateLimiter(name = "messagesLimit", fallbackMethod = "fallbackMethod")
    public ResponseEntity<Iterable<Message>> getMessages(@PathVariable("historyId") UUID historyId) throws Exception {
        return ResponseEntity.ok().body(messageService.getMessagesByHistoryId(historyId));
    }

    public ResponseEntity<Object> fallbackMethod(Exception e) throws Exception {
        if (e != null) throw e;

        throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, "Too many requests");
    }
}
