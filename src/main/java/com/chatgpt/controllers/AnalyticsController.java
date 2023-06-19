package com.chatgpt.controllers;

import com.chatgpt.services.ApiKeysService;
import com.chatgpt.websockets.OnlineWebsocketHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
public class AnalyticsController {
    @Autowired
    ApiKeysService apiKeysService;

    @Autowired
    private OnlineWebsocketHandler onlineWebsocketHandler;

    @GetMapping(path = "/analytics/online")
    public int getOnlineUsers() {
        return onlineWebsocketHandler.getOnlineUsers().size();
    }

    @GetMapping(path = "/analytics/keys/{keyType}")
    public HashMap<String, Integer> getAnalyticsKey(@PathVariable("keyType") String keyType) {
        return apiKeysService.getApiKeysMap(keyType);
    }
}
