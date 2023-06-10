package com.chatgpt.controllers;

import com.chatgpt.services.VkService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class VkController {

    @Autowired
    VkService vkService;

    @GetMapping(path = "/groups.isMember")
    Boolean groupsIsMember(@RequestParam String groupId, @RequestParam String userId) throws JsonProcessingException {
        return vkService.groupIsMember(groupId, userId);
    }
}
