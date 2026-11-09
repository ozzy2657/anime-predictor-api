package com.AnimeProject.demo;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


class AnimeRecorderTest {

    @Test
    void shouldStoreNameAndHighRatedState() {
        AnimeRecorder recorder = new AnimeRecorder( 12, "Action", 50000, 8.5, "TV");


        assertEquals(12, recorder.getEpisodes());
        assertEquals("Action", recorder.getGenre());
        assertEquals(50000, recorder.getMembers());
        assertEquals(8.5, recorder.getRating());
        assertEquals("TV", recorder.getType());

    }
}
