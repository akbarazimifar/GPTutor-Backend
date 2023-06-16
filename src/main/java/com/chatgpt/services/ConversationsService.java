package com.chatgpt.services;

import com.chatgpt.entity.ChatGptRequest;
import com.chatgpt.entity.ConversationRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;


@Service
public class ConversationsService {
    @Autowired
    ApiKeysService apiKeysService;

    public SseEmitter getConversation(ConversationRequest conversationRequest) throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        ChatGptRequest chatGptRequest = new ChatGptRequest(conversationRequest.getModel(), conversationRequest.getMessages(), true);
        String input = mapper.writeValueAsString(chatGptRequest);

        Utf8SseEmitter emitter = new Utf8SseEmitter();

        fetchCompletion(emitter, input, 0);

        return emitter;
    }

    public void fetchCompletion(Utf8SseEmitter emitter, String input,int attempt) {
        String apiKey = apiKeysService.getKey();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.openai.com/v1/chat/completions"))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + apiKey)
                .POST(HttpRequest.BodyPublishers.ofString(input))
                .build();


        HttpClient.newHttpClient().sendAsync(request, respInfo ->
        {
            if (respInfo.statusCode() == 200) {
                return new SseSubscriber((data) -> {
                    SseEmitter.SseEventBuilder event = SseEmitter.event()
                            .data(data);

                    if (data.equals("[DONE]")) emitter.complete();
                    try {
                        emitter.send(event);
                    } catch (IOException e) {
                        emitter.completeWithError(e);
                    }
                });
            }

            try {
                if (attempt == 15) {
                    emitter.send("[Error]:[" + respInfo.statusCode() + "]");
                    emitter.complete();
                    return null;
                }

                Thread.sleep(5000);
                fetchCompletion(emitter, input, attempt + 1);
            } catch (IOException | InterruptedException e) {
                emitter.completeWithError(e);
            }


            return null;
        });
    }
};
