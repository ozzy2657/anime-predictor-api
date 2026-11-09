package com.AnimeProject.demo;

import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AnimeRepositoryService {

    private final AnimeRepository animeRepository;

    public AnimeRepositoryService(AnimeRepository animeRepository) {
        this.animeRepository = animeRepository;
    }

    public AnimeRecordEntity save(AnimeRecordEntity entity) {
        return animeRepository.save(entity);
    }

    public Optional<AnimeRecordEntity> findById(Long id) {
        return animeRepository.findById(id);
    }
}
