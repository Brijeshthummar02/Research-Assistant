package com.research.assistant;

import lombok.Data;

@Data    // this annotation adds relevant getter and setters
public class ResearchRequest {
    private String content;
    private String operation;
}