package com.AnimeProject.demo;

import org.springframework.data.repository.CrudRepository;

public interface AnimeRepository extends CrudRepository<AnimeRecordEntity, Long> {
}
