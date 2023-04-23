package com.example.chatgpt.services;

import com.example.chatgpt.entity.Conversation;
import com.example.chatgpt.lrucache.LRUCache;

import java.util.Optional;

public class ConversationsService {
    private final LRUCache<String, String> lruCache = new LRUCache<String, String>(200);

    public boolean hasConversation(String name) {
        return lruCache.get(name).isPresent();
    }

    public Conversation getConversation(String name) {
        final Optional<String> optional = lruCache.get(name);

        return optional.map(message -> new Conversation(name, message)).orElse(null);
    }

    public boolean setConversation(Conversation conversation) {
        return lruCache.put(conversation.name, conversation.message);
    }
}
