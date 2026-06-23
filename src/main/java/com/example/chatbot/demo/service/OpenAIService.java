package com.example.chatbot.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class OpenAIService {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${gemini.api.key}")
    private String apiKey;

    @Value("${gemini.api.url}")
    private String url;

    public String askQuestion(String question) {
        try {
            if (question == null || question.trim().isEmpty()) {
                return "Please enter a valid question.";
            }

            String fullUrl = url + "?key=" + apiKey;

            Map<String, Object> textPart = Map.of(
                    "text", question
            );

            Map<String, Object> content = Map.of(
                    "parts", List.of(textPart)
            );

            Map<String, Object> requestBody = Map.of(
                    "contents", List.of(content)
            );

            ResponseEntity<Map> response =
                    restTemplate.postForEntity(
                            fullUrl,
                            requestBody,
                            Map.class
                    );

            Map<String, Object> body = response.getBody();

            if (body == null || !body.containsKey("candidates")) {
                return "No response received from Gemini.";
            }

            List<Map<String, Object>> candidates =
                    (List<Map<String, Object>>) body.get("candidates");

            if (candidates.isEmpty()) {
                return "Gemini returned no candidates.";
            }

            Map<String, Object> firstCandidate = candidates.get(0);
            Map<String, Object> candidateContent =
                    (Map<String, Object>) firstCandidate.get("content");

            List<Map<String, Object>> parts =
                    (List<Map<String, Object>>) candidateContent.get("parts");

            if (parts == null || parts.isEmpty()) {
                return "Gemini returned no text.";
            }

            return parts.get(0).get("text").toString();

        } catch (Exception e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }
}