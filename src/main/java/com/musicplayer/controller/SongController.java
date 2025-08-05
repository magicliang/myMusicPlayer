package com.musicplayer.controller;

import com.musicplayer.entity.Song;
import com.musicplayer.service.MusicService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/songs")
@CrossOrigin(origins = "*")
public class SongController {

    @Autowired
    private MusicService musicService;

    @GetMapping
    public ResponseEntity<List<Song>> getAllSongs() {
        return ResponseEntity.ok(musicService.getAllSongs());
    }

    @GetMapping("/{songId}")
    public ResponseEntity<Song> getSong(@PathVariable Long songId) {
        Song song = musicService.getSongById(songId);
        if (song != null) {
            return ResponseEntity.ok(song);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<Song>> searchSongs(@RequestParam String query) {
        return ResponseEntity.ok(musicService.searchSongs(query));
    }

    @PostMapping
    public ResponseEntity<Song> createSong(@RequestBody Song song) {
        Song newSong = musicService.createSong(
                song.getTitle(),
                song.getArtist().getId(),
                song.getAlbum() != null ? song.getAlbum().getId() : null,
                song.getDuration(),
                song.getAudioUrl()
        );
        return ResponseEntity.ok(newSong);
    }
}