package com.example.chatgpt.services;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;


public class ApiKeysService {
    private final HashMap<Integer, Integer> apiKeysMap = new HashMap();

    public ApiKeysService() {
        scheduleReset();
        reset();
    }

    void reset() {
        apiKeysMap.put(0, 3);
        apiKeysMap.put(1, 3);
    }

    private void scheduleReset() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() { public void run() { reset(); } }, 0, 60 * 1000);
    }

    public Integer getKeyIndex() {
        for (HashMap.Entry<Integer, Integer> item : apiKeysMap.entrySet()) {
            if (item.getValue() != 0) {
                apiKeysMap.replace(item.getKey(), item.getValue() - 1);
                System.out.println(apiKeysMap);
                return item.getKey();
            }
        }

        return null;
    }
}
