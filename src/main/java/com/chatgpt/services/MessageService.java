package com.chatgpt.services;

import com.chatgpt.entity.CreateMessageRequest;
import com.chatgpt.entity.Message;
import com.chatgpt.repositories.HistoryRepository;
import com.chatgpt.repositories.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
public class MessageService {
    @Autowired
    UserService userService;

    @Autowired
    HistoryRepository historyRepository;

    @Autowired
    MessageRepository messageRepository;

   public Message createMessage(String vkUserId, CreateMessageRequest createMessageRequest) throws Exception {
        var history = historyRepository.findById(createMessageRequest.getHistoryId());
        if (history.isEmpty()) throw new Exception("Not a found");

       checkHistory(vkUserId, createMessageRequest.getHistoryId());

        var message = new Message(
                history.get(),
                createMessageRequest.getContent(),
                createMessageRequest.getRole(),
                createMessageRequest.isError(),
                createMessageRequest.isFailedModeration(),
                createMessageRequest.getLastUpdated(),
                createMessageRequest.isInLocal()

        );

        history.get().setLastMessage(message.getContent());
        history.get().setLastUpdated(createMessageRequest.getLastUpdated());


        messageRepository.save(message);

        return message;

    }

    public Iterable<Message> getMessagesByHistoryId(String vkUserId, UUID historyId) {
        checkHistory(vkUserId, historyId);

        return messageRepository.findAllByHistoryIdOrderByCreatedAtAsc(historyId);
    }

    private void checkHistory(String vkUserId, UUID historyId) {
        var user = userService.getOrCreateVkUser(vkUserId);
        var foundHistory = historyRepository.findById(historyId);

        if (foundHistory.isPresent()) {
            if (user.getId() != foundHistory.get().getVkUser().getId()) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN);
            }
        }
    }
}
