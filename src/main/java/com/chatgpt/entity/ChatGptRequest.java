package com.chatgpt.entity;

public record ChatGptRequest(String model, Message[] messages, boolean stream) {
}
