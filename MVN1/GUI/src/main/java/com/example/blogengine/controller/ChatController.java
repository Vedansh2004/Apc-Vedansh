package com.example.blogengine.controller;

import com.example.blogengine.service.ChatService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@Controller
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @GetMapping("/chat")
    public String chatPage() {
        return "chat"; // renders templates/chat.html
    }

    @PostMapping("/api/chat")
    @ResponseBody
    public Map<String, Object> chatApi(@RequestBody Map<String, String> body, Principal principal) {
        String message = body != null ? body.getOrDefault("message", "") : "";
        String username = principal != null ? principal.getName() : null;
        String reply = chatService.reply(message, username);
        Map<String, Object> res = new HashMap<>();
        res.put("reply", reply);
        return res;
    }
}
