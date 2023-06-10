package com.chatgpt.services;

import com.chatgpt.entity.CreateMessageRequest;
import com.chatgpt.entity.Message;
import com.chatgpt.repositories.HistoryRepository;
import com.chatgpt.repositories.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class MessageService {
    @Autowired
    HistoryRepository historyRepository;

    @Autowired
    MessageRepository messageRepository;

   public Message createMessage(CreateMessageRequest createMessageRequest) throws Exception {
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

    public Iterable<Message> getMessagesByHistoryId(UUID historyId) {
        return messageRepository.findAllByHistoryIdOrderByCreatedAtAsc(historyId);
    }
}
