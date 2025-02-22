package com.research.assistant;

import org.springframework.stereotype.Service;

@Service
public class ResearchService {
    public String processContent(ResearchRequest request) {
        //Build the prompt
        String prompt = buildPrompt(request);

        // Query the AI model API
        // Parse the response
        // Return response
    }

    private String buildPrompt(ResearchRequest request) {
        StringBuilder prompt = new StringBuilder();
        switch (request.getOperation()) {
            case "Summarize":
                prompt.append("Summarize the following text concisely, highlighting the key points and main ideas in a few sentences. Ensure clarity and brevity:\n\n");
                break;
            case "Suggest":
                prompt.append("Analyze the following content and suggest related topics, further reading materials, and potential next steps. Organize the response with clear headings and bullet points for better readability:\n\n");
                break;
            default:
                throw new IllegalArgumentException("Unknown operation: " + request.getOperation());

        }

        prompt.append(request.getContent());
        return prompt.toString();
    }
