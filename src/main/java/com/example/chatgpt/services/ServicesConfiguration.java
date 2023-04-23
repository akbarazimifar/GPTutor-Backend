package com.example.chatgpt.services;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan
public class ServicesConfiguration {

    @Bean
    ApiKeysService apiKeysService() {
        return new ApiKeysService();
    }

    @Bean ConversationsService conversationsService() {
        return  new ConversationsService();
    }

}
