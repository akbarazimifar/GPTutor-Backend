package com.chatgpt;

import com.chatgpt.services.ApiKeysService;
import com.chatgpt.services.ConversationsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
;
@Configuration
public class ChatGptConfig {
    @Bean
    ApiKeysService apiKeysService() {
        return new ApiKeysService();
    }
    @Bean
    ConversationsService conversationsService() {
        return  new ConversationsService();
    }
}
