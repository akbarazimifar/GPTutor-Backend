package com.chatgpt;

import com.chatgpt.entity.*;
import com.chatgpt.repositories.HistoryRepository;
import com.chatgpt.repositories.MessageRepository;
import com.chatgpt.repositories.VkUsersRepository;
import com.chatgpt.services.ApiKeysService;
import com.chatgpt.services.ConversationsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.UUID;


@RestController("/")
public class ChatGptController {
    @Autowired
    MessageRepository messageRepository;

    @Autowired
    VkUsersRepository vkUsersRepository;

    @Autowired
    HistoryRepository historyRepository;

    ApiKeysService apiKeysService;
    ConversationsService conversationsService;

    public ChatGptController(ApiKeysService apiKeysService, ConversationsService conversationsService) {
        this.apiKeysService = apiKeysService;
        this.conversationsService = conversationsService;
    }

    @PostMapping(path = "/conversation", consumes = MediaType.APPLICATION_JSON_VALUE)
    public <T> T getConversation(@RequestBody ConversationRequest conversationRequest) throws IOException {
        System.out.println(this.apiKeysService.getKey());
        return (T) conversationsService.getConversation(conversationRequest, this.apiKeysService.getKey());
    }

    @PostMapping(path = "/user")
    public VkUser createVkUser(@RequestBody VkUser vkuser) {
        System.out.println(vkuser.getId());
        var foundFoundUser = vkUsersRepository.findByVkId(vkuser.getVkId());
        if (foundFoundUser != null) return foundFoundUser;

        vkUsersRepository.save(vkuser);

        return vkuser;
    }

    @PostMapping(path = "/history")
    public History createHistory(@RequestBody CreateHistoryRequest createHistoryRequest) throws Exception {
        var user = vkUsersRepository.findById(createHistoryRequest.getUserVkId());
        if (user.isEmpty()) throw new Exception("Not a found");

        var history = new History(
                user.get(),
                createHistoryRequest.getLastMessage(),
                createHistoryRequest.getType(),
                createHistoryRequest.getSystemMessage(),
                createHistoryRequest.getLessonName(),
                createHistoryRequest.getLastUpdated()
        );

        historyRepository.save(history);

        return history;
    }

    @GetMapping(path = "/history/{userId}")
    public Iterable<History> getHistoryById(@PathVariable("userId") UUID userId) throws Exception {
        return historyRepository.findAllByVkUserId(userId);
    }

    @DeleteMapping(path = "/history/{id}")
    @Transactional
    public void deleteHistory(@PathVariable("id") UUID historyId) {
        messageRepository.deleteAllByHistoryId(historyId);
        historyRepository.deleteById(historyId);
    }

    @PostMapping(path = "/messages")
    public Message createMessage(@RequestBody CreateMessageRequest createMessageRequest) throws Exception {
        var history = historyRepository.findById(createMessageRequest.getHistoryId());
        if (history.isEmpty()) throw new Exception("Not a found");

        var message = new Message(
                history.get(),
                createMessageRequest.getContent(),
                createMessageRequest.getRole(),
                createMessageRequest.isError(),
                createMessageRequest.isFailedModeration(),
                createMessageRequest.getLastUpdated()
        );

        history.get().setLastMessage(message.getContent());
        history.get().setLastUpdated(createMessageRequest.getLastUpdated());


        messageRepository.save(message);

        return message;
    }

    @GetMapping(path = "/messages/{historyId}")
    public Iterable<Message> getMessages(@PathVariable("historyId") UUID historyId) throws Exception {
        return messageRepository.findAllByHistoryIdOrderByCreatedAtAsc(historyId);
    }
}
