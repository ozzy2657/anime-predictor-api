package com.AnimeProject.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class AnimeServiceTest {

    @Autowired
    private AnimeService animeService;

    @Test
    void shouldPredictRatingForKnownInput() {
        double result = animeService.predictRating(12, 50000, "Action", "TV");

        assertEquals(8.19148929116, result, 0.0001);
    }
}
