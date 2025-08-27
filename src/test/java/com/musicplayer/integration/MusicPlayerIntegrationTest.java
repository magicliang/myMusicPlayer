package com.musicplayer.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.musicplayer.entity.Album;
import com.musicplayer.entity.Artist;
import com.musicplayer.entity.Song;
import com.musicplayer.entity.User;
import com.musicplayer.repository.AlbumRepository;
import com.musicplayer.repository.ArtistRepository;
import com.musicplayer.repository.SongRepository;
import com.musicplayer.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class MusicPlayerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private AlbumRepository albumRepository;

    @Autowired
    private SongRepository songRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private User testUser;
    private Artist testArtist;
    private Album testAlbum;
    private Song testSong;

    @BeforeEach
    void setUp() {
        // Clean up
        songRepository.deleteAll();
        albumRepository.deleteAll();
        artistRepository.deleteAll();
        userRepository.deleteAll();

        // Create test data
        testUser = new User();
        testUser.setUsername("testuser");
        testUser.setEmail("test@example.com");
        testUser.setPassword("password123");
        testUser = userRepository.save(testUser);

        testArtist = new Artist();
        testArtist.setName("Test Artist");
        testArtist.setBio("Test Bio");
        testArtist = artistRepository.save(testArtist);

        testAlbum = new Album();
        testAlbum.setTitle("Test Album");
        testAlbum.setArtist(testArtist);
        testAlbum.setGenre("Rock");
        testAlbum = albumRepository.save(testAlbum);

        testSong = new Song();
        testSong.setTitle("Test Song");
        testSong.setArtist(testArtist);
        testSong.setAlbum(testAlbum);
        testSong.setDuration(180);
        testSong.setAudioUrl("http://example.com/song.mp3");
        testSong = songRepository.save(testSong);
    }

    @Test
    void testCompleteUserWorkflow() throws Exception {
        // Test getting all songs
        mockMvc.perform(get("/api/songs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].title").value("Test Song"));

        // Test getting specific song
        mockMvc.perform(get("/api/songs/" + testSong.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test Song"))
                .andExpect(jsonPath("$.artist.name").value("Test Artist"));

        // Test searching songs
        mockMvc.perform(get("/api/songs/search").param("query", "Test"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Test Song"));
    }
}