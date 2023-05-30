package com.chatgpt.repositories;

import com.chatgpt.entity.History;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface HistoryRepository extends CrudRepository<History, UUID> {
    Iterable<History> findAllByVkUserId(UUID vkId);
}
