package com.musicplayer.controller;

import com.musicplayer.entity.Album;
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
@RequestMapping("/api/albums")
@CrossOrigin(origins = "*")
public class AlbumController {

    @Autowired
    private MusicService musicService;

    @GetMapping
    public ResponseEntity<List<Album>> getAllAlbums() {
        return ResponseEntity.ok(musicService.getAllAlbums());
    }

    @GetMapping("/{albumId}")
    public ResponseEntity<Album> getAlbum(@PathVariable Long albumId) {
        Album album = musicService.getAlbumById(albumId);
        if (album != null) {
            return ResponseEntity.ok(album);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<Album> createAlbum(@RequestBody Album album) {
        Album newAlbum = musicService.createAlbum(
                album.getTitle(),
                album.getArtist().getId(),
                album.getCoverImageUrl(),
                album.getGenre()
        );
        return ResponseEntity.ok(newAlbum);
    }

    @GetMapping("/artist/{artistId}")
    public ResponseEntity<List<Album>> getAlbumsByArtist(@PathVariable Long artistId) {
        return ResponseEntity.ok(musicService.getAlbumsByArtistId(artistId));
    }

    @GetMapping("/search")
    public ResponseEntity<List<Album>> searchAlbums(@RequestParam String query) {
        return ResponseEntity.ok(musicService.searchAlbums(query));
    }
}