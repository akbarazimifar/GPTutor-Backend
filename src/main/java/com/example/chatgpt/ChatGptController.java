package com.example.chatgpt;

import com.example.chatgpt.entity.Conversation;
import com.example.chatgpt.entity.Result;
import com.example.chatgpt.seestring.SSEString;
import com.example.chatgpt.services.ApiKeysService;
import com.example.chatgpt.services.ConversationsService;
import org.springframework.expression.spel.ast.NullLiteral;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import static org.springframework.http.HttpStatus.NOT_FOUND;


@CrossOrigin(value = "*")
@RestController("/")
public class ChatGptController {
    ApiKeysService apiKeysService;
    ConversationsService conversationsService;

    public ChatGptController(ApiKeysService apiKeysService, ConversationsService conversationsService) {
            this.apiKeysService = apiKeysService;
            this.conversationsService = conversationsService;
    }

    @PostMapping(path = "/cache", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Result<NullLiteral>> setCache(@RequestBody Conversation conversation) {
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(new Result<>(conversationsService.setConversation(conversation), null));
    }


    @GetMapping(path = "/cache", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter getCache(@RequestParam String conversationName) {
        final Conversation conversation = conversationsService.getConversation(conversationName);
        if (conversation == null) throw new ResponseStatusException(NOT_FOUND, "Completion not a found");;

        return new SSEString().streamString(conversation.message);
    }

    @GetMapping("/api-keys-index")
    public Integer checkCache() {
        return this.apiKeysService.getKeyIndex();
    }
}
