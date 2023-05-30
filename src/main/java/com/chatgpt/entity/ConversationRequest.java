package com.chatgpt.entity;

public class ConversationRequest {
    private ConversationMessage[] messages;
    private String model;

    public ConversationMessage[] getMessages() {
        return messages;
    }

    public void setNumbers(ConversationMessage[] messages) {
        this.messages = messages;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }
}
