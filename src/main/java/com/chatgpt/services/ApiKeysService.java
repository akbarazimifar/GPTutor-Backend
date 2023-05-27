package com.chatgpt.services;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;

import java.util.*;



public class ApiKeysService implements InitializingBean {
    final int ATTEMPTS = 3;

    @Value("${api.keys}")
    private String apiKeys;
    private HashMap<String, Integer> apiKeysMap = new HashMap<>();

    public void afterPropertiesSet()
    {
      setupApiKeysMap();
      scheduleReset();
    }

    void setupApiKeysMap() {
        final List<String> splitApiKeys = Arrays.asList(this.apiKeys.split(","));
        final HashMap<String, Integer> hashMap = new HashMap<>();

        splitApiKeys.forEach((item) -> hashMap.put(item, ATTEMPTS));

        this.apiKeysMap = hashMap;
    }

    public String getKey() {
        for (HashMap.Entry<String, Integer> item : apiKeysMap.entrySet()) {
            if (item.getValue() != 0) {
                apiKeysMap.replace(item.getKey(), item.getValue() - 1);
                return item.getKey();
            }
        }

        return null;
    }

    private void scheduleReset() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() { public void run() { setupApiKeysMap(); } }, 0, 60 * 1000);
    }
}
