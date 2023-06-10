package com.chatgpt.controllers;

import com.chatgpt.services.VkService;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class VkController {

    @Autowired
    VkService vkService;

    @GetMapping(path = "/groups-is-member")
    @RateLimiter(name = "vkLimit", fallbackMethod = "fallbackMethod")
    Boolean groupsIsMember(@RequestParam String groupId, @RequestParam String userId) throws JsonProcessingException {
        return vkService.groupIsMember(groupId, userId);
    }

    public ResponseEntity<Object> fallbackMethod(Exception e) {
        throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, "Too many requests");
    }
}
