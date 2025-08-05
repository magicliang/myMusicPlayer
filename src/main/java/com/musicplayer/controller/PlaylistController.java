package com.musicplayer.controller;

import com.musicplayer.entity.Playlist;
import com.musicplayer.service.MusicService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/playlists")
@CrossOrigin(origins = "*")
public class PlaylistController {

    @Autowired
    private MusicService musicService;
    
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Playlist>> getUserPlaylists(@PathVariable Long userId) {
        return ResponseEntity.ok(musicService.getUserPlaylists(userId));
    }

    @GetMapping("/{playlistId}")
    public ResponseEntity<Playlist> getPlaylist(@PathVariable Long playlistId) {
        Playlist playlist = musicService.playlistRepository.findById(playlistId).orElse(null);
        if (playlist != null) {
            return ResponseEntity.ok(playlist);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<Playlist> createPlaylist(@RequestBody Playlist playlist) {
        Playlist newPlaylist = musicService.createPlaylist(
                playlist.getOwner().getId(),
                playlist.getName(),
                playlist.getDescription()
        );
        return ResponseEntity.ok(newPlaylist);
    }

    @PostMapping("/{playlistId}/songs/{songId}")
    public ResponseEntity<Playlist> addSongToPlaylist(
            @PathVariable Long playlistId,
            @PathVariable Long songId) {
        Playlist playlist = musicService.addSongToPlaylist(playlistId, songId);
        if (playlist != null) {
            return ResponseEntity.ok(playlist);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{playlistId}/songs/{songId}")
    public ResponseEntity<Playlist> removeSongFromPlaylist(
            @PathVariable Long playlistId,
            @PathVariable Long songId) {
        Playlist playlist = musicService.removeSongFromPlaylist(playlistId, songId);
        if (playlist != null) {
            return ResponseEntity.ok(playlist);
        }
        return ResponseEntity.notFound().build();
    }
}