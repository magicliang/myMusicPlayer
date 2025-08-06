package com.musicplayer.controller;

import com.musicplayer.entity.PlayerState;
import com.musicplayer.service.MusicService;
import com.musicplayer.service.StateSyncService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/player")
@CrossOrigin(origins = "*")
public class PlayerController {

    @Autowired
    private MusicService musicService;

    @Autowired
    private StateSyncService stateSyncService;

    @GetMapping("/state/{userId}")
    public ResponseEntity<PlayerState> getPlayerState(@PathVariable Long userId) {
        PlayerState state = musicService.getPlayerState(userId);
        if (state != null) {
            return ResponseEntity.ok(state);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/play/{userId}/{songId}")
    public ResponseEntity<PlayerState> playSong(
            @PathVariable Long userId,
            @PathVariable Long songId) {
        PlayerState state = musicService.playSong(userId, songId);
        if (state != null) {
            stateSyncService.broadcastStateChange(userId, state);
            return ResponseEntity.ok(state);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/pause/{userId}")
    public ResponseEntity<PlayerState> pauseSong(@PathVariable Long userId) {
        PlayerState state = musicService.pauseSong(userId);
        if (state != null) {
            stateSyncService.broadcastStateChange(userId, state);
            return ResponseEntity.ok(state);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/resume/{userId}")
    public ResponseEntity<PlayerState> resumeSong(@PathVariable Long userId) {
        PlayerState state = musicService.resumeSong(userId);
        if (state != null) {
            stateSyncService.broadcastStateChange(userId, state);
            return ResponseEntity.ok(state);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/seek/{userId}")
    public ResponseEntity<PlayerState> seekSong(
            @PathVariable Long userId,
            @RequestParam Integer timestamp) {
        PlayerState state = musicService.seekSong(userId, timestamp);
        if (state != null) {
            stateSyncService.broadcastStateChange(userId, state);
            return ResponseEntity.ok(state);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/update-state/{userId}")
    public ResponseEntity<PlayerState> updatePlayerState(
            @PathVariable Long userId,
            @RequestBody PlayerStateUpdateRequest request) {
        PlayerState state = musicService.updatePlayerState(
                userId,
                request.getSongId(),
                request.getTimestamp(),
                request.getStatus(),
                request.getQueue()
        );
        if (state != null) {
            stateSyncService.broadcastStateChange(userId, state);
            return ResponseEntity.ok(state);
        }
        return ResponseEntity.notFound().build();
    }

    // WebSocket message handler
    @MessageMapping("/player-event")
    public void handlePlayerEvent(@Payload PlayerEvent event) {
        stateSyncService.handlePlayerEvent(
                event.getUserId(),
                event.getAction(),
                event.getSongId(),
                event.getTimestamp()
        );
    }

    public static class PlayerStateUpdateRequest {
        private Long songId;
        private Integer timestamp;
        private PlayerState.PlayerStatus status;
        private List<Long> queue;

        // Getters and setters
        public Long getSongId() {
            return songId;
        }

        public void setSongId(Long songId) {
            this.songId = songId;
        }

        public Integer getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(Integer timestamp) {
            this.timestamp = timestamp;
        }

        public PlayerState.PlayerStatus getStatus() {
            return status;
        }

        public void setStatus(PlayerState.PlayerStatus status) {
            this.status = status;
        }

        public List<Long> getQueue() {
            return queue;
        }

        public void setQueue(List<Long> queue) {
            this.queue = queue;
        }
    }

    public static class PlayerEvent {
        private Long userId;
        private String action;
        private Long songId;
        private Integer timestamp;

        // Getters and setters
        public Long getUserId() {
            return userId;
        }

        public void setUserId(Long userId) {
            this.userId = userId;
        }

        public String getAction() {
            return action;
        }

        public void setAction(String action) {
            this.action = action;
        }

        public Long getSongId() {
            return songId;
        }

        public void setSongId(Long songId) {
            this.songId = songId;
        }

        public Integer getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(Integer timestamp) {
            this.timestamp = timestamp;
        }
    }
}