package com.example.chatgpt.entity;

public class Conversation {
    public String name;
    public String message;

    public Conversation(String name, String message) {
        this.name = name;
        this.message = message;
    }

    String getName() {
        return name;
    }

    String getMessage() {
        return message;
    }

    void setName(String name) {
        this.name = name;
    }

    void setMessage(String message) {
        this.message = message;
    }
}
