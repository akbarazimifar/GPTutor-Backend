package com.chatgpt.entity;

import java.util.UUID;

public class CreateHistoryRequest {
    private UUID userVkId;

    private String lastMessage;

    private String type;

    private String lessonName;

    private String systemMessage;

    public CreateHistoryRequest() {
    }

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

    public String getSystemMessage() {
        return systemMessage;
    }

    public void setSystemMessage(String systemMessage) {
        this.systemMessage = systemMessage;
    }

    public String getLessonName() {
        return lessonName;
    }

    public void setLessonName(String lessonName) {
        this.lessonName = lessonName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
