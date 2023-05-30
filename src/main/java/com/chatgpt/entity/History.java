package com.chatgpt.entity;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
public class History {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @ManyToOne(fetch = FetchType.EAGER)
    private VkUser vkUser;
    private String lastMessage;
    private String title;
    private String systemMessage;
    public History() {
    }
    public History(VkUser vkUser, String lastMessage, String title, String systemMessage) {
        this.vkUser = vkUser;
        this.lastMessage = lastMessage;
        this.title = title;
        this.systemMessage = systemMessage;

    }
    public VkUser getVkUser() {
        return vkUser;
    }
    public void setVkUser(VkUser vkUser) {
        this.vkUser = vkUser;
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

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}
