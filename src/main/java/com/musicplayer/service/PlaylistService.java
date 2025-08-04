package com.musicplayer.service;

import com.musicplayer.dto.PlaylistDTO;
import com.musicplayer.entity.Playlist;
import com.musicplayer.entity.PlaylistSong;
import com.musicplayer.entity.Song;
import com.musicplayer.entity.User;
import com.musicplayer.repository.PlaylistRepository;
import com.musicplayer.repository.PlaylistSongRepository;
import com.musicplayer.repository.SongRepository;
import com.musicplayer.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlaylistService {

    @Autowired
    private PlaylistRepository playlistRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SongRepository songRepository;

    @Autowired
    private PlaylistSongRepository playlistSongRepository;

    public Playlist createPlaylist(PlaylistDTO playlistDTO) {
        User owner = userRepository.findById(playlistDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Playlist playlist = new Playlist();
        playlist.setName(playlistDTO.getName());
        playlist.setDescription(playlistDTO.getDescription());
        playlist.setIsPublic(playlistDTO.getIsPublic());
        playlist.setOwner(owner);

        return playlistRepository.save(playlist);
    }

    public Optional<Playlist> getPlaylistById(Long id) {
        return playlistRepository.findById(id);
    }

    public List<Playlist> getUserPlaylists(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return playlistRepository.findByOwner(user);
    }

    public List<Playlist> getPublicPlaylists() {
        return playlistRepository.findByIsPublicTrue();
    }

    public List<Playlist> searchPlaylists(String query) {
        return playlistRepository.findByNameContainingIgnoreCase(query);
    }

    public Playlist updatePlaylist(Long id, PlaylistDTO playlistDTO) {
        Playlist playlist = playlistRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Playlist not found"));

        playlist.setName(playlistDTO.getName());
        playlist.setDescription(playlistDTO.getDescription());
        playlist.setIsPublic(playlistDTO.getIsPublic());

        return playlistRepository.save(playlist);
    }

    public void deletePlaylist(Long id) {
        if (!playlistRepository.existsById(id)) {
            throw new RuntimeException("Playlist not found");
        }
        playlistRepository.deleteById(id);
    }

    public void addSongToPlaylist(Long playlistId, Long songId) {
        Playlist playlist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new RuntimeException("Playlist not found"));
        Song song = songRepository.findById(songId)
                .orElseThrow(() -> new RuntimeException("Song not found"));

        if (playlistSongRepository.existsByPlaylistAndSong(playlist, song)) {
            throw new RuntimeException("Song already exists in playlist");
        }

        int position = playlistSongRepository.findByPlaylist(playlist).size();

        PlaylistSong playlistSong = new PlaylistSong();
        playlistSong.setPlaylist(playlist);
        playlistSong.setSong(song);
        playlistSong.setPosition(position);

        playlistSongRepository.save(playlistSong);
    }

    public void removeSongFromPlaylist(Long playlistId, Long songId) {
        Playlist playlist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new RuntimeException("Playlist not found"));
        Song song = songRepository.findById(songId)
                .orElseThrow(() -> new RuntimeException("Song not found"));

        playlistSongRepository.deleteByPlaylistAndSong(playlist, song);
    }

    public List<Song> getPlaylistSongs(Long playlistId) {
        Playlist playlist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new RuntimeException("Playlist not found"));

        return playlistSongRepository.findByPlaylistOrderByPosition(playlist)
                .stream()
                .map(PlaylistSong::getSong)
                .collect(Collectors.toList());
    }

    public List<PlaylistDTO> convertToDTOList(List<Playlist> playlists) {
        return playlists.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public PlaylistDTO convertToDTO(Playlist playlist) {
        PlaylistDTO dto = new PlaylistDTO();
        dto.setId(playlist.getId());
        dto.setName(playlist.getName());
        dto.setDescription(playlist.getDescription());
        dto.setIsPublic(playlist.getIsPublic());
        dto.setUserId(playlist.getOwner().getId());
        return dto;
    }
}
