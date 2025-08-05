package com.musicplayer.controller;

import com.musicplayer.entity.Artist;
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
@RequestMapping("/api/artists")
@CrossOrigin(origins = "*")
public class ArtistController {

    @Autowired
    private MusicService musicService;

    @GetMapping
    public ResponseEntity<List<Artist>> getAllArtists() {
        return ResponseEntity.ok(musicService.getAllArtists());
    }

    @GetMapping("/{artistId}")
    public ResponseEntity<Artist> getArtist(@PathVariable Long artistId) {
        Artist artist = musicService.artistRepository.findById(artistId).orElse(null);
        if (artist != null) {
            return ResponseEntity.ok(artist);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<Artist> createArtist(@RequestBody Artist artist) {
        Artist newArtist = musicService.createArtist(
                artist.getName(),
                artist.getBio(),
                artist.getImageUrl()
        );
        return ResponseEntity.ok(newArtist);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Artist>> searchArtists(@RequestParam String query) {
        return ResponseEntity.ok(musicService.artistRepository.findByNameContainingIgnoreCase(query));
    }
}