package com.example.chatbot.demo.controller;

import com.example.chatbot.demo.dto.ChatRequest;
import com.example.chatbot.demo.dto.ChatResponse;
import com.example.chatbot.demo.service.OpenAIService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ChatController {

    private final OpenAIService openAIService;

    public ChatController(OpenAIService openAIService) {
        this.openAIService = openAIService;
    }

    @GetMapping("/test")
    public String test() {
        return "Application Working";
    }

    @PostMapping("/chat")
    public ChatResponse chat(@RequestBody ChatRequest request) {

        String answer =
                openAIService.askQuestion(
                        request.getMessage()
                );

        return new ChatResponse(answer);
    }
}