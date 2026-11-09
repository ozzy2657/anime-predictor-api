package com.AnimeProject.demo;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import java.util.Collections;

@RestController
@RequestMapping("/animes")
public class AnimeController {
    // Service that contains the prediction logic. Spring will inject
    // an instance of `AnimeService` when it creates this controller.
    private final AnimeService animeService;

    // Constructor injection: preferred in Spring because it makes the
    // dependency explicit and easier to test.
    public AnimeController(AnimeService animeService) {
        this.animeService = animeService;
    }

    // Browser-friendly health/info endpoint at /animes/
    @GetMapping("/")
    public ResponseEntity<?> home() {
        return ResponseEntity.ok(Collections.singletonMap(
            "message",
            "Anime predictor API is running. Use POST /animes/predict?episodes=...&members=...&genre=...&type=..."
        ));
    }

    // Handle GET requests to /animes/predict for browser testing
    @GetMapping("/predict")
    public ResponseEntity<?> predictGet(
        @RequestParam int episodes,
        @RequestParam int members,
        @RequestParam String genre,
        @RequestParam String type) {

        double result = animeService.predictRating(episodes, members, genre, type);
        return ResponseEntity.ok(Collections.singletonMap("predictedRating", result));
    }

    // Handle POST requests to /animes/predict
    // Example request path: POST /animes/predict?episodes=12&members=50000&genre=Action&type=TV
    @PostMapping("/predict")
    public ResponseEntity<?> predict(
        // @RequestParam binds an HTTP request parameter to the method argument.
        // If the parameter is missing the request will fail (required by default).
        @RequestParam int episodes,
        @RequestParam int members,
        @RequestParam String genre,
        @RequestParam String type) {
        // Basic input validation
        if (episodes <= 0) {
            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Collections.singletonMap("error", "episodes must be greater than 0"));
        }

        if (members < 0) {
            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Collections.singletonMap("error", "members cannot be negative"));
        }

        if (genre == null || genre.trim().isEmpty()) {
            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Collections.singletonMap("error", "genre is required"));
        }

        if (type == null || type.trim().isEmpty()) {
            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Collections.singletonMap("error", "type is required"));
        }

        // Call service to compute the predicted rating based on validated inputs.
        double result = animeService.predictRating(episodes, members, genre, type);

        // Return a structured JSON response by returning a Map — Spring
        // automatically converts it to JSON.
        return ResponseEntity.ok(Collections.singletonMap("predictedRating", result));
    }
}
