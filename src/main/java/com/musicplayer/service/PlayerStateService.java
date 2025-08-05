package com.musicplayer.service;

import com.musicplayer.dto.PlayerStateDTO;
import com.musicplayer.entity.PlayerState;
import com.musicplayer.entity.Song;
import com.musicplayer.entity.User;
import com.musicplayer.repository.PlayerStateRepository;
import com.musicplayer.repository.SongRepository;
import com.musicplayer.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlayerStateService {

    @Autowired
    private PlayerStateRepository playerStateRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SongRepository songRepository;

    public PlayerState getOrCreatePlayerState(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return playerStateRepository.findByUser(user)
                .orElseGet(() -> {
                    PlayerState newState = new PlayerState();
                    newState.setUser(user);
                    return playerStateRepository.save(newState);
                });
    }

    public PlayerState updatePlayerState(Long userId, PlayerStateDTO playerStateDTO) {
        PlayerState playerState = getOrCreatePlayerState(userId);

        if (playerStateDTO.getCurrentSongId() != null) {
            Song song = songRepository.findById(playerStateDTO.getCurrentSongId())
                    .orElseThrow(() -> new RuntimeException("Song not found"));
            playerState.setCurrentSong(song);
        } else {
            playerState.setCurrentSong(null);
        }

        if (playerStateDTO.getPlaybackPosition() != null) {
            playerState.setPlaybackPosition(playerStateDTO.getPlaybackPosition());
        }

        if (playerStateDTO.getIsPlaying() != null) {
            playerState.setIsPlaying(playerStateDTO.getIsPlaying());
        }

        if (playerStateDTO.getRepeatMode() != null) {
            playerState.setRepeatMode(playerStateDTO.getRepeatMode());
        }

        if (playerStateDTO.getShuffleMode() != null) {
            playerState.setShuffleMode(playerStateDTO.getShuffleMode());
        }

        if (playerStateDTO.getQueue() != null) {
            playerState.setQueue(playerStateDTO.getQueue());
        }

        return playerStateRepository.save(playerState);
    }

    public PlayerState playSong(Long userId, Long songId) {
        PlayerState playerState = getOrCreatePlayerState(userId);

        Song song = songRepository.findById(songId)
                .orElseThrow(() -> new RuntimeException("Song not found"));

        playerState.setCurrentSong(song);
        playerState.setPlaybackPosition(0);
        playerState.setIsPlaying(true);

        return playerStateRepository.save(playerState);
    }

    public PlayerState pauseSong(Long userId) {
        PlayerState playerState = getOrCreatePlayerState(userId);
        playerState.setIsPlaying(false);
        return playerStateRepository.save(playerState);
    }

    public PlayerState resumeSong(Long userId) {
        PlayerState playerState = getOrCreatePlayerState(userId);
        playerState.setIsPlaying(true);
        return playerStateRepository.save(playerState);
    }

    public PlayerState seekTo(Long userId, Integer position) {
        PlayerState playerState = getOrCreatePlayerState(userId);
        playerState.setPlaybackPosition(position);
        return playerStateRepository.save(playerState);
    }

    public PlayerStateDTO convertToDTO(PlayerState playerState) {
        PlayerStateDTO dto = new PlayerStateDTO();
        dto.setCurrentSongId(playerState.getCurrentSong() != null ? playerState.getCurrentSong().getId() : null);
        dto.setPlaybackPosition(playerState.getPlaybackPosition());
        dto.setIsPlaying(playerState.getIsPlaying());
        dto.setRepeatMode(playerState.getRepeatMode());
        dto.setShuffleMode(playerState.getShuffleMode());
        dto.setQueue(playerState.getQueue());
        return dto;
    }
}