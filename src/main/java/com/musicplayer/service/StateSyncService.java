package com.musicplayer.service;

import com.musicplayer.entity.PlayerState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class StateSyncService {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private MusicService musicService;

    public void notifyClients(Long userId, PlayerState state) {
        messagingTemplate.convertAndSendToUser(
                userId.toString(),
                "/queue/player-state",
                state
        );
    }

    public void broadcastStateChange(Long userId, PlayerState state) {
        notifyClients(userId, state);
    }

    public void handlePlayerEvent(Long userId, String action, Long songId, Integer timestamp) {
        PlayerState state = null;

        switch (action.toUpperCase()) {
            case "PLAY":
                state = musicService.playSong(userId, songId);
                break;
            case "PAUSE":
                state = musicService.pauseSong(userId);
                break;
            case "RESUME":
                state = musicService.resumeSong(userId);
                break;
            case "SEEK":
                state = musicService.seekSong(userId, timestamp);
                break;
            default:
                state = musicService.getPlayerState(userId);
        }

        if (state != null) {
            broadcastStateChange(userId, state);
        }
    }
}