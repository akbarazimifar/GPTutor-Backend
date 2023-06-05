package com.chatgpt.repositories;

import com.chatgpt.entity.Message;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface MessageRepository extends CrudRepository<Message, UUID> {
    Iterable<Message> findAllByHistoryIdOrderByCreatedAtAsc(UUID id);
    void deleteAllByHistoryId(UUID id);
}
