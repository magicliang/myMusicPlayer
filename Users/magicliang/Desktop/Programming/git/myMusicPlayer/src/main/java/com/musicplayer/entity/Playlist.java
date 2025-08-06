package com.musicplayer.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

@Entity
@Table(name = "playlists")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Playlist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String name;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;
    
    @OneToMany(mappedBy = "playlist", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("position ASC")
    private List<PlaylistSong> playlistSongs = new ArrayList<>();

    // 便捷方法获取歌曲列表
    public List<Song> getSongs() {
        return playlistSongs.stream()
                .map(PlaylistSong::getSong)
                .collect(Collectors.toList());
    }

    // 设置歌曲列表的方法
    public void setSongs(List<Song> songs) {
        this.playlistSongs.clear();
        for (int i = 0; i < songs.size(); i++) {
            PlaylistSong playlistSong = new PlaylistSong();
            playlistSong.setPlaylist(this);
            playlistSong.setSong(songs.get(i));
            playlistSong.setPosition(i);
            this.playlistSongs.add(playlistSong);
        }
    }
    
    private String description;
    
    private String coverImageUrl;
    
    @Column(nullable = false)
    private Boolean isPublic = false;
    
    public Boolean getIsPublic() {
        return isPublic;
    }
    
    public void setIsPublic(Boolean isPublic) {
        this.isPublic = isPublic;
    }
    
    @Column(name = "created_at")
    private java.time.LocalDateTime createdAt = java.time.LocalDateTime.now();
}