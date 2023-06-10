package com.chatgpt.services;

import com.chatgpt.entity.CreateHistoryRequest;
import com.chatgpt.entity.History;
import com.chatgpt.repositories.HistoryRepository;
import com.chatgpt.repositories.MessageRepository;
import com.chatgpt.repositories.VkUsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class HistoryService {

    @Autowired
    UserService userService;

    @Autowired
    VkUsersRepository vkUsersRepository;

    @Autowired
    HistoryRepository historyRepository;

    @Autowired
    MessageRepository messageRepository;

    public History createHistory(String vkUserId, CreateHistoryRequest createHistoryRequest) throws Exception {
        var user = userService.getOrCreateVkUser(vkUserId);

        var history = new History(
                user,
                createHistoryRequest.getLastMessage(),
                createHistoryRequest.getType(),
                createHistoryRequest.getSystemMessage(),
                createHistoryRequest.getLessonName(),
                createHistoryRequest.getLastUpdated()
        );

        historyRepository.save(history);

        return history;
    }


    public Iterable<History> getAllHistory(String vkUserId) {
        var user = userService.getOrCreateVkUser(vkUserId);
        return historyRepository.findAllByVkUserId(user.getId());
    }

    public void deleteHistory(UUID historyId) {
        messageRepository.deleteAllByHistoryId(historyId);
        historyRepository.deleteById(historyId);
    }
}
