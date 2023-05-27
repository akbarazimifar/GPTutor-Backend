package com.chatgpt;

import com.chatgpt.entity.ConversationRequest;
import com.chatgpt.services.ApiKeysService;
import com.chatgpt.services.ConversationsService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;


@RestController("/")
public class ChatGptController {
    ApiKeysService apiKeysService;
    ConversationsService conversationsService;

    public ChatGptController(ApiKeysService apiKeysService, ConversationsService conversationsService) {
        this.apiKeysService = apiKeysService;
        this.conversationsService = conversationsService;
    }

    @PostMapping(path = "/conversation", consumes = MediaType.APPLICATION_JSON_VALUE)
    public <T> T getConversation(@RequestBody ConversationRequest conversationRequest) throws IOException {
        return (T)conversationsService.getConversation(conversationRequest, this.apiKeysService.getKey());
    }
}
