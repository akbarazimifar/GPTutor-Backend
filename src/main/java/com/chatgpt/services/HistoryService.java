package com.chatgpt.services;

import com.chatgpt.entity.CreateHistoryRequest;
import com.chatgpt.entity.History;
import com.chatgpt.repositories.HistoryRepository;
import com.chatgpt.repositories.MessageRepository;
import com.chatgpt.repositories.VkUsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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


    public Page<History> getAllHistory(String vkUserId, int pageNumber, int pageSize) {
        PageRequest pageable = PageRequest.of(pageNumber, pageSize, Sort.by("lastUpdated").descending());
        var user = userService.getOrCreateVkUser(vkUserId);
        return historyRepository.findAllByVkUserId(user.getId(),  pageable);
    }

    public void deleteHistory(UUID historyId) {
        messageRepository.deleteAllByHistoryId(historyId);
        historyRepository.deleteById(historyId);
    }
}
