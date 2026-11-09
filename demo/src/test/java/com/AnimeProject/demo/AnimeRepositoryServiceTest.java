package com.AnimeProject.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class AnimeRepositoryServiceTest {

    @Autowired
    private AnimeRepositoryService animeRepositoryService;

    @Test
    void shouldFindSavedAnimeRecordById() {
        AnimeRecordEntity saved = animeRepositoryService.save(new AnimeRecordEntity("Naruto", "Action", "TV"));
        Optional<AnimeRecordEntity> found = animeRepositoryService.findById(saved.getId());

        assertTrue(found.isPresent());
    }
}
