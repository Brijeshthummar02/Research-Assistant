package com.research.assistant;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/research")
@CrossOrigin(origins = "*")
@AllArgsConstructor  // imported from lombok, makes all argument constructor with  all fields u define
public class ResearchController {
    private final ResearchService researchService;

    @PostMapping("/process") // linking to the post request
    public ResponseEntity<String> processContent(@RequestBody ResearchRequest request) {     //This is END point
        String result = researchService.processContent(request);
        return ResponseEntity.ok(result);
    }
}