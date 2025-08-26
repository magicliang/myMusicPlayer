package com.musicplayer.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.musicplayer.entity.Album;
import com.musicplayer.entity.Artist;
import com.musicplayer.entity.Song;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
class SongRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private SongRepository songRepository;

    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private AlbumRepository albumRepository;

    private Artist artist;
    private Album album;
    private Song song;

    @BeforeEach
    void setUp() {
        artist = new Artist();
        artist.setName("Test Artist");
        artist.setBio("Test Bio");
        artist = entityManager.persistAndFlush(artist);

        album = new Album();
        album.setTitle("Test Album");
        album.setArtist(artist);
        album.setGenre("Rock");
        album = entityManager.persistAndFlush(album);

        song = new Song();
        song.setTitle("Test Song");
        song.setArtist(artist);
        song.setAlbum(album);
        song.setDuration(180);
        song.setAudioUrl("http://example.com/song.mp3");
        song = entityManager.persistAndFlush(song);
    }

    @Test
    void testFindById() {
        Optional<Song> found = songRepository.findById(song.getId());
        assertTrue(found.isPresent());
        assertEquals("Test Song", found.get().getTitle());
    }

    @Test
    void testFindAll() {
        List<Song> songs = songRepository.findAll();
        assertFalse(songs.isEmpty());
        assertEquals(1, songs.size());
    }

    @Test
    void testSearchByTitleOrArtist() {
        List<Song> songs = songRepository.searchByTitleOrArtist("Test");
        assertFalse(songs.isEmpty());
        assertEquals(1, songs.size());
        assertEquals("Test Song", songs.get(0).getTitle());
    }

    @Test
    void testSearchByArtistName() {
        List<Song> songs = songRepository.searchByTitleOrArtist("Test Artist");
        assertFalse(songs.isEmpty());
        assertEquals(1, songs.size());
    }

    @Test
    void testSave() {
        Song newSong = new Song();
        newSong.setTitle("New Song");
        newSong.setArtist(artist);
        newSong.setAlbum(album);
        newSong.setDuration(200);
        newSong.setAudioUrl("http://example.com/newsong.mp3");

        Song saved = songRepository.save(newSong);
        assertNotNull(saved.getId());
        assertEquals("New Song", saved.getTitle());
    }

    @Test
    void testDelete() {
        Long songId = song.getId();
        songRepository.delete(song);
        entityManager.flush();

        Optional<Song> found = songRepository.findById(songId);
        assertFalse(found.isPresent());
    }
}