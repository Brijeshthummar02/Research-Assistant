package com.research.assistant;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Service
public class ResearchService {
    @Value("${gemini.api.url}")
    private String geminiApiUrl;

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    public ResearchService(WebClient.Builder webClientBuilder, ObjectMapper objectMapper) {
        this.webClient = webClientBuilder.build();
        this.objectMapper = objectMapper;
    }


    public String processContent(ResearchRequest request) {
        // Build the prompt
        String prompt = buildPrompt(request);

        // Query the AI Model API
        Map<String, Object> requestBody = Map.of(
                "contents", new Object[] {
                        Map.of("parts", new Object[]{
                                Map.of("text", prompt)
                        })
                }
        );

        String response = webClient.post()
                .uri(geminiApiUrl + geminiApiKey)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        // Parse the response
        // Return response

        return extractTextFromResponse(response);
    }

    private String extractTextFromResponse(String response) {
        try {
            GeminiResponse geminiResponse = objectMapper.readValue(response, GeminiResponse.class);
            if (geminiResponse.getCandidates() != null && !geminiResponse.getCandidates().isEmpty()) {
                GeminiResponse.Candidate firstCandidate = geminiResponse.getCandidates().get(0);
                if (firstCandidate.getContent() != null &&
                        firstCandidate.getContent().getParts() != null &&
                        !firstCandidate.getContent().getParts().isEmpty()) {
                    return firstCandidate.getContent().getParts().get(0).getText();
                }
            }
            return "No content found in response";
        } catch (Exception e) {
            return "Error Parsing: " + e.getMessage();
        }
    }

    private String buildPrompt(ResearchRequest request) {
        StringBuilder prompt = new StringBuilder();
        switch (request.getOperation()) {
            case "summarize":
                prompt.append("Summarize the following text concisely while preserving the key points, core message, and important details. \n\n");
                prompt.append("### **Guidelines:** \n");
                prompt.append("- Extract the **main ideas** and **key takeaways** without unnecessary details. \n");
                prompt.append("- Present the summary using **clear bullet points** for better readability. \n");
                prompt.append("- Maintain **logical flow** while ensuring brevity and clarity. \n");
                prompt.append("- Avoid redundancy and repetitive information. \n");
                prompt.append("- If the text contains any **numbers, statistics, or important names**, retain them accurately. \n");
                prompt.append("- If any **links or URLs** are found, mention them separately at the end and display them with blue-highlighted words (e.g., [Source](https://example.com)). \n\n");
                prompt.append("### **Text to Summarize:** \n\n");
                prompt.append(request.getContent());
                break;
            case "suggest":
                prompt.append("Analyze the following content and suggest related topics, further reading materials, and potential next steps. Organize the response with clear headings and bullet points for better readability:\n\n");
                break;
            default:
                throw new IllegalArgumentException("Unknown operation: " + request.getOperation());

        }
        prompt.append(request.getContent());
        return prompt.toString();
    }
}



