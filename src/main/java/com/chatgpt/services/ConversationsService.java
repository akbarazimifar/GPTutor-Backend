package com.chatgpt.services;

import com.chatgpt.entity.ChatGptRequest;
import com.chatgpt.entity.ConversationRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;


public class ConversationsService {
    public SseEmitter getConversation(ConversationRequest conversationRequest, String key) throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        ChatGptRequest chatGptRequest = new ChatGptRequest(conversationRequest.getModel(), conversationRequest.getMessages(), true);
        String input = mapper.writeValueAsString(chatGptRequest);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.openai.com/v1/chat/completions"))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + key)
                .POST(HttpRequest.BodyPublishers.ofString(input))
                .build();

        Utf8SseEmitter emitter = new Utf8SseEmitter();


        HttpClient.newHttpClient().sendAsync(request, respInfo ->
        {
            if (respInfo.statusCode() == 200) {
                return new SseSubscriber((data) -> {
                    SseEmitter.SseEventBuilder event = SseEmitter.event()
                            .data(data);

                    if (data.equals("[DONE]"))  emitter.complete();
                    try {  emitter.send(event); }
                    catch (IOException e) { emitter.completeWithError(e); }
                });
            }

            try {
                emitter.send("[Error]:[" + respInfo.statusCode() + "]");
                emitter.complete();
            } catch (IOException e) { emitter.completeWithError(e); }


            return null;
        });

        return emitter;
    }
};
