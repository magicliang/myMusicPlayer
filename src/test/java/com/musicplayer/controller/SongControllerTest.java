package com.musicplayer.controller;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.musicplayer.entity.Album;
import com.musicplayer.entity.Artist;
import com.musicplayer.entity.Song;
import com.musicplayer.service.MusicService;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(SongController.class)
@ActiveProfiles("test")
class SongControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MusicService musicService;

    @Autowired
    private ObjectMapper objectMapper;

    private Song testSong;
    private Artist testArtist;
    private Album testAlbum;

    @BeforeEach
    void setUp() {
        testArtist = new Artist();
        testArtist.setId(1L);
        testArtist.setName("Test Artist");

        testAlbum = new Album();
        testAlbum.setId(1L);
        testAlbum.setTitle("Test Album");
        testAlbum.setArtist(testArtist);

        testSong = new Song();
        testSong.setId(1L);
        testSong.setTitle("Test Song");
        testSong.setArtist(testArtist);
        testSong.setAlbum(testAlbum);
        testSong.setDuration(180);
        testSong.setAudioUrl("http://example.com/song.mp3");
    }

    @Test
    void testGetAllSongs() throws Exception {
        List<Song> songs = Arrays.asList(testSong);
        when(musicService.getAllSongs()).thenReturn(songs);

        mockMvc.perform(get("/api/songs"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].title").value("Test Song"));
    }

    @Test
    void testGetSong() throws Exception {
        when(musicService.getSongById(1L)).thenReturn(testSong);

        mockMvc.perform(get("/api/songs/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Test Song"))
                .andExpect(jsonPath("$.duration").value(180));
    }

    @Test
    void testGetSongNotFound() throws Exception {
        when(musicService.getSongById(999L)).thenReturn(null);

        mockMvc.perform(get("/api/songs/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testSearchSongs() throws Exception {
        List<Song> songs = Arrays.asList(testSong);
        when(musicService.searchSongs("Test")).thenReturn(songs);

        mockMvc.perform(get("/api/songs/search").param("query", "Test"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].title").value("Test Song"));
    }

    @Test
    void testCreateSong() throws Exception {
        when(musicService.createSong(anyString(), anyLong(), anyLong(), anyInt(), anyString()))
                .thenReturn(testSong);

        String songJson = objectMapper.writeValueAsString(testSong);

        mockMvc.perform(post("/api/songs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(songJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title").value("Test Song"));
    }
}