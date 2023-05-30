package com.chatgpt.entity;

import java.util.UUID;

public class DeleteHistoryRequest {
    private UUID historyId;

    public UUID getHistoryId() {
        return historyId;
    }

    public void setHistoryId(UUID historyId) {
        this.historyId = historyId;
    }
}
