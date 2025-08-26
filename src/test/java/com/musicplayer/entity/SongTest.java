package com.musicplayer.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SongTest {

    private Song song;
    private Artist artist;
    private Album album;

    @BeforeEach
    void setUp() {
        artist = new Artist();
        artist.setId(1L);
        artist.setName("Test Artist");

        album = new Album();
        album.setId(1L);
        album.setTitle("Test Album");

        song = new Song();
        song.setId(1L);
        song.setTitle("Test Song");
        song.setArtist(artist);
        song.setAlbum(album);
        song.setDuration(180);
        song.setAudioUrl("http://example.com/song.mp3");
    }

    @Test
    void testSongCreation() {
        assertNotNull(song);
        assertEquals(1L, song.getId());
        assertEquals("Test Song", song.getTitle());
        assertEquals(artist, song.getArtist());
        assertEquals(album, song.getAlbum());
        assertEquals(180, song.getDuration());
        assertEquals("http://example.com/song.mp3", song.getAudioUrl());
    }

    @Test
    void testSongEquality() {
        Song song2 = new Song();
        song2.setId(1L);
        song2.setTitle("Test Song");
        song2.setArtist(artist);
        song2.setAlbum(album);
        song2.setDuration(180);
        song2.setAudioUrl("http://example.com/song.mp3");

        assertEquals(song, song2);
        assertEquals(song.hashCode(), song2.hashCode());
    }

    @Test
    void testSongToString() {
        String songString = song.toString();
        assertTrue(songString.contains("Test Song"));
        assertTrue(songString.contains("180"));
    }
}