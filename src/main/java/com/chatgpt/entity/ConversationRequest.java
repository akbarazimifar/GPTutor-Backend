package com.chatgpt.entity;

public class ConversationRequest {
    private Message[] messages;
    private String model;

    public Message[] getMessages() {
        return messages;
    }

    public void setNumbers(Message[] messages) {
        this.messages = messages;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }
}
