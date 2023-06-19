package com.chatgpt.services;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class ApiKeysService implements InitializingBean {
    final int ATTEMPTS_5_DOLLARS = 3;
    final int ATTEMPTS_120_DOLLARS = 72;

    @Value("${api.keys.5dollars}")
    private String apiKeys5dollars;

    @Value("${api.keys.120dollars}")
    private String apiKeys120dollars;

    private HashMap<String, Integer> apiKeysMap5dollars = new HashMap<>();
    private HashMap<String, Integer> apiKeysMap120dollars = new HashMap<>();

    public HashMap<String, Integer> getApiKeysMap(String keyType) {
        if (Objects.equals(keyType, "5")) {
            return this.apiKeysMap5dollars;
        }

        if (Objects.equals(keyType, "120")) {
            return this.apiKeysMap120dollars;
        }

        return new HashMap<>();
    }


    public void afterPropertiesSet() {
        setupApiKeysMap();
        scheduleReset();
    }

    void setupApiKeysMap() {
        final List<String> splitApiKeys5dollars = Arrays.asList(this.apiKeys5dollars.split(","));
        final List<String> splitApiKeys120dollars = Arrays.asList(this.apiKeys120dollars.split(","));

        final HashMap<String, Integer> hashMap5dollars = new HashMap<>();
        if (this.apiKeys5dollars.length() > 0) {
            splitApiKeys5dollars.forEach((item) -> hashMap5dollars.put(item, ATTEMPTS_5_DOLLARS));
        }

        final HashMap<String, Integer> hashMap120dollars = new HashMap<>();

        if (this.apiKeys120dollars.length() > 0) {
            splitApiKeys120dollars.forEach((item) -> hashMap120dollars.put(item, ATTEMPTS_120_DOLLARS));
        }

        this.apiKeysMap5dollars = hashMap5dollars;
        this.apiKeysMap120dollars = hashMap120dollars;
    }


    void refreshApiKeysMap5Dollars() {
        for (HashMap.Entry<String, Integer> item : apiKeysMap5dollars.entrySet()) {
            if (item.getValue() != ATTEMPTS_5_DOLLARS) {
                apiKeysMap5dollars.replace(item.getKey(), item.getValue() + 1);
            }
        }
    }

    void refreshApiKeysMap120Dollars() {
        for (HashMap.Entry<String, Integer> item : apiKeysMap120dollars.entrySet()) {
            if (item.getValue() != ATTEMPTS_120_DOLLARS) {
                apiKeysMap120dollars.replace(item.getKey(), ATTEMPTS_120_DOLLARS);
            }
        }
    }

    public String getKey5dollars() {
        for (HashMap.Entry<String, Integer> item : apiKeysMap5dollars.entrySet()) {
            if (item.getValue() != 0) {
                apiKeysMap5dollars.replace(item.getKey(), item.getValue() - 1);
                return item.getKey();
            }
        }

        return null;
    }

    public String getKey120dollars() {
        for (HashMap.Entry<String, Integer> item : apiKeysMap120dollars.entrySet()) {
            if (item.getValue() != 0) {
                apiKeysMap120dollars.replace(item.getKey(), item.getValue() - 1);
                System.out.println(item.getKey());
                return item.getKey();
            }
        }

        return null;
    }

    public Pair<String, String> getKey() {
        var key = getKey5dollars();
        if (key != null) return Pair.of(key, "5");

        return Pair.of(getKey120dollars(), "120");
    }

    private void scheduleReset() {
        Timer timer5 = new Timer();
        timer5.schedule(new TimerTask() {
            public void run() {
                refreshApiKeysMap5Dollars();
            }
        }, 0, 20 * 1000);

        Timer timer120 = new Timer();
        timer120.schedule(new TimerTask() {
            public void run() {
                refreshApiKeysMap120Dollars();
            }
        }, 0, 60 * 1000);
    }

    public void detachKey(Pair<String, String> key) {
        if (Objects.equals(key.getSecond(), "5")) {
            apiKeysMap5dollars.replace(key.getFirst(), 0);
        }

        if (Objects.equals(key.getSecond(), "120")) {
            apiKeysMap120dollars.replace(key.getFirst(), 0);
        }
    }

}
