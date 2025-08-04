package com.musicplayer.controller;

import com.musicplayer.dto.ApiResponse;
import com.musicplayer.dto.PlaylistDTO;
import com.musicplayer.entity.Playlist;
import com.musicplayer.entity.Song;
import com.musicplayer.service.PlaylistService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/playlists")
@CrossOrigin(origins = "*")
public class PlaylistController {

    @Autowired
    private PlaylistService playlistService;

    @PostMapping
    public ResponseEntity<ApiResponse<Playlist>> createPlaylist(@Valid @RequestBody PlaylistDTO playlistDTO) {
        try {
            Playlist playlist = playlistService.createPlaylist(playlistDTO);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("Playlist created successfully", playlist));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<PlaylistDTO>>> getAllPlaylists() {
        List<Playlist> playlists = playlistService.getPublicPlaylists();
        List<PlaylistDTO> playlistDTOs = playlistService.convertToDTOList(playlists);
        return ResponseEntity.ok(ApiResponse.success(playlistDTOs));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PlaylistDTO>> getPlaylistById(@PathVariable Long id) {
        return playlistService.getPlaylistById(id)
                .map(playlist -> ResponseEntity.ok(ApiResponse.success(playlistService.convertToDTO(playlist))))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<PlaylistDTO>>> getUserPlaylists(@PathVariable Long userId) {
        List<Playlist> playlists = playlistService.getUserPlaylists(userId);
        List<PlaylistDTO> playlistDTOs = playlistService.convertToDTOList(playlists);
        return ResponseEntity.ok(ApiResponse.success(playlistDTOs));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<PlaylistDTO>>> searchPlaylists(@RequestParam String query) {
        List<Playlist> playlists = playlistService.searchPlaylists(query);
        List<PlaylistDTO> playlistDTOs = playlistService.convertToDTOList(playlists);
        return ResponseEntity.ok(ApiResponse.success(playlistDTOs));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Playlist>> updatePlaylist(
            @PathVariable Long id,
            @Valid @RequestBody PlaylistDTO playlistDTO) {
        try {
            Playlist playlist = playlistService.updatePlaylist(id, playlistDTO);
            return ResponseEntity.ok(ApiResponse.success("Playlist updated successfully", playlist));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deletePlaylist(@PathVariable Long id) {
        try {
            playlistService.deletePlaylist(id);
            return ResponseEntity.ok(ApiResponse.success("Playlist deleted successfully", null));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @PostMapping("/{playlistId}/songs/{songId}")
    public ResponseEntity<ApiResponse<Void>> addSongToPlaylist(
            @PathVariable Long playlistId,
            @PathVariable Long songId) {
        try {
            playlistService.addSongToPlaylist(playlistId, songId);
            return ResponseEntity.ok(ApiResponse.success("Song added to playlist successfully", null));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @DeleteMapping("/{playlistId}/songs/{songId}")
    public ResponseEntity<ApiResponse<Void>> removeSongFromPlaylist(
            @PathVariable Long playlistId,
            @PathVariable Long songId) {
        try {
            playlistService.removeSongFromPlaylist(playlistId, songId);
            return ResponseEntity.ok(ApiResponse.success("Song removed from playlist successfully", null));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/{playlistId}/songs")
    public ResponseEntity<ApiResponse<List<Song>>> getPlaylistSongs(@PathVariable Long playlistId) {
        try {
            List<Song> songs = playlistService.getPlaylistSongs(playlistId);
            return ResponseEntity.ok(ApiResponse.success(songs));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        }
    }
}
