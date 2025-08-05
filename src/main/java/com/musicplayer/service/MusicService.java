package com.musicplayer.service;

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
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MusicService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SongRepository songRepository;

    @Autowired
    private PlaylistRepository playlistRepository;

    @Autowired
    private PlayerStateRepository playerStateRepository;

    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private AlbumRepository albumRepository;

    // User operations
    public User createUser(String username, String password, String email) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password); // In real app, use password encoder
        user.setEmail(email);
        return userRepository.save(user);
    }

    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElse(null);
    }

    // Song operations
    public Song createSong(String title, Long artistId, Long albumId, Integer duration, String audioUrl) {
        Song song = new Song();
        song.setTitle(title);
        song.setArtist(artistRepository.findById(artistId).orElse(null));
        song.setAlbum(albumRepository.findById(albumId).orElse(null));
        song.setDuration(duration);
        song.setAudioUrl(audioUrl);
        return songRepository.save(song);
    }

    public List<Song> getAllSongs() {
        return songRepository.findAll();
    }

    public Song getSongById(Long songId) {
        return songRepository.findById(songId).orElse(null);
    }

    public List<Song> searchSongs(String query) {
        return songRepository.searchByTitleOrArtist(query);
    }

    // Playlist operations
    public Playlist createPlaylist(Long userId, String name, String description) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return null;
        }

        Playlist playlist = new Playlist();
        playlist.setName(name);
        playlist.setDescription(description);
        playlist.setOwner(user);
        return playlistRepository.save(playlist);
    }

    public Playlist addSongToPlaylist(Long playlistId, Long songId) {
        Playlist playlist = playlistRepository.findById(playlistId).orElse(null);
        Song song = songRepository.findById(songId).orElse(null);

        if (playlist == null || song == null) {
            return null;
        }

        playlist.getSongs().add(song);
        return playlistRepository.save(playlist);
    }

    public Playlist removeSongFromPlaylist(Long playlistId, Long songId) {
        Playlist playlist = playlistRepository.findById(playlistId).orElse(null);
        if (playlist == null) {
            return null;
        }

        playlist.getSongs().removeIf(song -> song.getId().equals(songId));
        return playlistRepository.save(playlist);
    }

    public List<Playlist> getUserPlaylists(Long userId) {
        return playlistRepository.findByOwnerId(userId);
    }

    // Player state operations
    public PlayerState getPlayerState(Long userId) {
        return playerStateRepository.findByUserId(userId).orElseGet(() -> {
            User user = userRepository.findById(userId).orElse(null);
            if (user == null) {
                return null;
            }

            PlayerState state = new PlayerState();
            state.setUser(user);
            state.setStatus(PlayerState.PlayerStatus.STOPPED);
            state.setCurrentTimestamp(0);
            return playerStateRepository.save(state);
        });
    }

    public PlayerState updatePlayerState(Long userId, Long songId, Integer timestamp,
            PlayerState.PlayerStatus status, List<Long> queue) {
        PlayerState state = getPlayerState(userId);
        if (state == null) {
            return null;
        }

        if (songId != null) {
            Song song = songRepository.findById(songId).orElse(null);
            state.setCurrentSong(song);
        }

        if (timestamp != null) {
            state.setCurrentTimestamp(timestamp);
        }

        if (status != null) {
            state.setStatus(status);
        }

        if (queue != null) {
            state.setQueueOrder(queue.stream().map(String::valueOf).collect(Collectors.joining(",")));
        }

        state.setUpdatedAt(new java.util.Date().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime());
        return playerStateRepository.save(state);
    }

    public PlayerState playSong(Long userId, Long songId) {
        return updatePlayerState(userId, songId, 0, PlayerState.PlayerStatus.PLAYING, null);
    }

    public PlayerState pauseSong(Long userId) {
        PlayerState state = getPlayerState(userId);
        if (state != null) {
            state.setStatus(PlayerState.PlayerStatus.PAUSED);
            state.setUpdatedAt(
                    new java.util.Date().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime());
            return playerStateRepository.save(state);
        }
        return null;
    }

    public PlayerState resumeSong(Long userId) {
        PlayerState state = getPlayerState(userId);
        if (state != null) {
            state.setStatus(PlayerState.PlayerStatus.PLAYING);
            state.setUpdatedAt(
                    new java.util.Date().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime());
            return playerStateRepository.save(state);
        }
        return null;
    }

    public PlayerState seekSong(Long userId, Integer timestamp) {
        PlayerState state = getPlayerState(userId);
        if (state != null) {
            state.setCurrentTimestamp(timestamp);
            state.setUpdatedAt(
                    new java.util.Date().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime());
            return playerStateRepository.save(state);
        }
        return null;
    }

    // Artist operations
    public Artist createArtist(String name, String bio, String imageUrl) {
        Artist artist = new Artist();
        artist.setName(name);
        artist.setBio(bio);
        artist.setImageUrl(imageUrl);
        return artistRepository.save(artist);
    }

    public List<Artist> getAllArtists() {
        return artistRepository.findAll();
    }

    // Album operations
    public Album createAlbum(String title, Long artistId, String coverImageUrl, String genre) {
        Album album = new Album();
        album.setTitle(title);
        album.setArtist(artistRepository.findById(artistId).orElse(null));
        album.setCoverImageUrl(coverImageUrl);
        album.setGenre(genre);
        album.setReleaseDate(java.time.LocalDate.now());
        return albumRepository.save(album);
    }

    public List<Album> getAllAlbums() {
        return albumRepository.findAll();
    }
}