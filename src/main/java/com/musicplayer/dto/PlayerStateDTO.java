package com.musicplayer.dto;

import com.musicplayer.entity.PlayerState;
import java.util.List;

public class PlayerStateDTO {

    private Long currentSongId;
    private Integer playbackPosition;
    private Boolean isPlaying;
    private PlayerState.RepeatMode repeatMode;
    private Boolean shuffleMode;
    private List<Long> queue;

    // Getters and Setters
    public Long getCurrentSongId() {
        return currentSongId;
    }

    public void setCurrentSongId(Long currentSongId) {
        this.currentSongId = currentSongId;
    }

    public Integer getPlaybackPosition() {
        return playbackPosition;
    }

    public void setPlaybackPosition(Integer playbackPosition) {
        this.playbackPosition = playbackPosition;
    }

    public Boolean getIsPlaying() {
        return isPlaying;
    }

    public void setIsPlaying(Boolean isPlaying) {
        this.isPlaying = isPlaying;
    }

    public PlayerState.RepeatMode getRepeatMode() {
        return repeatMode;
    }

    public void setRepeatMode(PlayerState.RepeatMode repeatMode) {
        this.repeatMode = repeatMode;
    }

    public Boolean getShuffleMode() {
        return shuffleMode;
    }

    public void setShuffleMode(Boolean shuffleMode) {
        this.shuffleMode = shuffleMode;
    }

    public List<Long> getQueue() {
        return queue;
    }

    public void setQueue(List<Long> queue) {
        this.queue = queue;
    }
}