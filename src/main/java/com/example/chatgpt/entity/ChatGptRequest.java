package com.example.chatgpt.entity;

public record ChatGptRequest(String model, Message[] messages, boolean stream) {
}
