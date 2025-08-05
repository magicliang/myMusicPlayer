package com.musicplayer.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "player_states")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlayerState {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "current_song_id")
    private Song currentSong;

    @Column(name = "current_timestamp")
    private Integer currentTimestamp = 0;

    @Enumerated(EnumType.STRING)
    private PlayerStatus status = PlayerStatus.STOPPED;

    @Enumerated(EnumType.STRING)
    private RepeatMode repeatMode = RepeatMode.NONE;

    private Boolean shuffleMode = false;

    @Column(name = "queue_order")
    private String queueOrder; // JSON string of song IDs

    @Column(name = "updated_at")
    private java.time.LocalDateTime updatedAt = java.time.LocalDateTime.now();

    public enum PlayerStatus {
        PLAYING, PAUSED, STOPPED
    }

    public enum RepeatMode {
        NONE, ONE, ALL
    }
}