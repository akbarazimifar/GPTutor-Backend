package com.example.chatgpt.seestring;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SSEString {
    public SseEmitter streamString(String str) {
        final String[] slittedString = splitString(str);

        SseEmitter emitter = new SseEmitter();
        ExecutorService executor = Executors.newSingleThreadExecutor();

        executor.execute(() -> {
            try {
                for (int index = 0; index < slittedString.length; index++) {
                    SseEmitter.SseEventBuilder event = SseEmitter.event()
                            .data(slittedString[index])
                            .id(String.valueOf(index));
                    emitter.send(event);
                    Thread.sleep(100);
                }

                emitter.complete();
            } catch (Exception ex) {
                emitter.completeWithError(ex);
            }
        });

        return emitter;

    }

    private String[] splitString(String str) {
        return str.replaceAll("`", "%60").replaceAll("\n", "%0A").split(" ");
    }
}
