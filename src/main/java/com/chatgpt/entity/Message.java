package com.chatgpt.entity;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @ManyToOne(fetch = FetchType.EAGER)
    private History history;
    private String content;
    private String role;
    private boolean isError;
    private boolean isFailedModeration;

    public Message() {
    }

    public Message(History history, String content, String role, boolean isError, boolean isFailedModeration) {
        this.history = history;
        this.content = content;
        this.role = role;
        this.isError = isError;
        this.isFailedModeration = isFailedModeration;
    }

    public History getHistory() {
        return history;
    }

    public void setHistory(History history) {
        this.history = history;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isError() {
        return isError;
    }

    public void setError(boolean error) {
        isError = error;
    }

    public boolean isFailedModeration() {
        return isFailedModeration;
    }

    public void setFailedModeration(boolean failedModeration) {
        isFailedModeration = failedModeration;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}
