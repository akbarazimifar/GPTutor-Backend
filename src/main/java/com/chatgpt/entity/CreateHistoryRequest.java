package com.chatgpt.entity;

import java.util.UUID;

public class CreateHistoryRequest {
    private UUID userVkId;
    private String lastMessage;
    private String title;

    private String systemMessage;

    public UUID getUserVkId() {
        return userVkId;
    }

    public void setUserVkId(UUID userVkId) {
        this.userVkId = userVkId;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSystemMessage() {
        return systemMessage;
    }

    public void setSystemMessage(String systemMessage) {
        this.systemMessage = systemMessage;
    }
}
