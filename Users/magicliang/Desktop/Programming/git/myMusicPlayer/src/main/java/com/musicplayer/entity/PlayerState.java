package com.musicplayer.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Enumerated(EnumType.STRING)
    private PlayerStatus status = PlayerStatus.STOPPED;
    
    @ElementCollection
    @CollectionTable(name = "player_state_queue", joinColumns = @JoinColumn(name = "player_state_id"))
    @Column(name = "song_id")
    private List<Long> queue = new ArrayList<>();
    
    @Column(name = "queue_order")
    private String queueOrder; // JSON string of song IDs
    
    public String getQueueOrder() {
        return queueOrder;
    }
    
    public void setQueueOrder(String queueOrder) {
        this.queueOrder = queueOrder;
    }
    
    @Column(name = "updated_at")
    private java.time.LocalDateTime updatedAt = java.time.LocalDateTime.now();
    
    @Column(name = "current_time_position")
    private Integer currentTimePosition = 0;
    
    public enum PlayerStatus {
        PLAYING, PAUSED, STOPPED
    }
    
    public enum RepeatMode {
        NONE, ONE, ALL
    }
    
    public Integer getCurrentTimePosition() {
        return currentTimePosition;
    }

    public void setCurrentTimePosition(Integer currentTimePosition) {
        this.currentTimePosition = currentTimePosition;
    }