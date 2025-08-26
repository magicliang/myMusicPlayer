package com.musicplayer.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.musicplayer.entity.Album;
import com.musicplayer.entity.Artist;
import com.musicplayer.entity.PlayerState;
import com.musicplayer.entity.Playlist;
import com.musicplayer.entity.Song;
import com.musicplayer.entity.User;
import com.musicplayer.repository.AlbumRepository;
import com.musicplayer.repository.ArtistRepository;
import com.musicplayer.repository.PlayerStateRepository;
import com.musicplayer.repository.PlaylistRepository;
import com.musicplayer.repository.SongRepository;
import com.musicplayer.repository.UserRepository;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MusicServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private SongRepository songRepository;

    @Mock
    private PlaylistRepository playlistRepository;

    @Mock
    private PlayerStateRepository playerStateRepository;

    @Mock
    private ArtistRepository artistRepository;

    @Mock
    private AlbumRepository albumRepository;

    @InjectMocks
    private MusicService musicService;

    private User testUser;
    private Song testSong;
    private Artist testArtist;
    private Album testAlbum;
    private Playlist testPlaylist;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");
        testUser.setEmail("test@example.com");

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

        testPlaylist = new Playlist();
        testPlaylist.setId(1L);
        testPlaylist.setName("Test Playlist");
        testPlaylist.setOwner(testUser);
    }

    @Test
    void testCreateUser() {
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        User result = musicService.createUser("testuser", "password", "test@example.com");

        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void testGetUserById() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        User result = musicService.getUserById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("testuser", result.getUsername());
    }

    @Test
    void testGetUserByIdNotFound() {
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        User result = musicService.getUserById(999L);

        assertNull(result);
    }

    @Test
    void testCreateSong() {
        when(artistRepository.findById(1L)).thenReturn(Optional.of(testArtist));
        when(albumRepository.findById(1L)).thenReturn(Optional.of(testAlbum));
        when(songRepository.save(any(Song.class))).thenReturn(testSong);

        Song result = musicService.createSong("Test Song", 1L, 1L, 180, "http://example.com/song.mp3");

        assertNotNull(result);
        assertEquals("Test Song", result.getTitle());
        verify(songRepository).save(any(Song.class));
    }

    @Test
    void testGetAllSongs() {
        List<Song> songs = Arrays.asList(testSong);
        when(songRepository.findAll()).thenReturn(songs);

        List<Song> result = musicService.getAllSongs();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test Song", result.get(0).getTitle());
    }

    @Test
    void testSearchSongs() {
        List<Song> songs = Arrays.asList(testSong);
        when(songRepository.searchByTitleOrArtist("Test")).thenReturn(songs);

        List<Song> result = musicService.searchSongs("Test");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test Song", result.get(0).getTitle());
    }

    @Test
    void testCreatePlaylist() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(playlistRepository.save(any(Playlist.class))).thenReturn(testPlaylist);

        Playlist result = musicService.createPlaylist(1L, "Test Playlist", "Description");

        assertNotNull(result);
        assertEquals("Test Playlist", result.getName());
        verify(playlistRepository).save(any(Playlist.class));
    }

    @Test
    void testCreatePlaylistUserNotFound() {
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        Playlist result = musicService.createPlaylist(999L, "Test Playlist", "Description");

        assertNull(result);
        verify(playlistRepository, never()).save(any(Playlist.class));
    }

    @Test
    void testGetPlayerState() {
        PlayerState playerState = new PlayerState();
        playerState.setUser(testUser);
        playerState.setStatus(PlayerState.PlayerStatus.STOPPED);

        when(playerStateRepository.findByUserId(1L)).thenReturn(Optional.of(playerState));

        PlayerState result = musicService.getPlayerState(1L);

        assertNotNull(result);
        assertEquals(PlayerState.PlayerStatus.STOPPED, result.getStatus());
    }

    @Test
    void testGetPlayerStateCreateNew() {
        when(playerStateRepository.findByUserId(1L)).thenReturn(Optional.empty());
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        PlayerState newState = new PlayerState();
        newState.setUser(testUser);
        newState.setStatus(PlayerState.PlayerStatus.STOPPED);
        when(playerStateRepository.save(any(PlayerState.class))).thenReturn(newState);

        PlayerState result = musicService.getPlayerState(1L);

        assertNotNull(result);
        assertEquals(PlayerState.PlayerStatus.STOPPED, result.getStatus());
        verify(playerStateRepository).save(any(PlayerState.class));
    }

    @Test
    void testPlaySong() {
        PlayerState playerState = new PlayerState();
        playerState.setUser(testUser);
        playerState.setCurrentSong(testSong);
        playerState.setStatus(PlayerState.PlayerStatus.PLAYING);

        when(playerStateRepository.findByUserId(1L)).thenReturn(Optional.of(playerState));
        when(songRepository.findById(1L)).thenReturn(Optional.of(testSong));
        when(playerStateRepository.save(any(PlayerState.class))).thenReturn(playerState);

        PlayerState result = musicService.playSong(1L, 1L);

        assertNotNull(result);
        assertEquals(PlayerState.PlayerStatus.PLAYING, result.getStatus());
        assertEquals(testSong, result.getCurrentSong());
    }
}