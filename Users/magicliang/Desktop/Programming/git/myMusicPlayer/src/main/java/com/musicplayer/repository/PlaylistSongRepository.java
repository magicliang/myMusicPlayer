package com.musicplayer.repository;

import com.musicplayer.entity.Playlist;
import com.musicplayer.entity.PlaylistSong;
import com.musicplayer.entity.Song;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface PlaylistSongRepository extends JpaRepository<PlaylistSong, Long> {
    List<PlaylistSong> findByPlaylistOrderByPosition(Playlist playlist);
    Optional<PlaylistSong> findByPlaylistAndSong(Playlist playlist, Song song);
    boolean existsByPlaylistAndSong(Playlist playlist, Song song);
    void deleteByPlaylistAndSong(Playlist playlist, Song song);
    List<PlaylistSong> findByPlaylist(Playlist playlist);
    
    @Query("SELECT MAX(ps.position) FROM PlaylistSong ps WHERE ps.playlist.id = :playlistId")
    Integer findMaxPositionByPlaylistId(@Param("playlistId") Long playlistId);
    
    @Query("SELECT ps FROM PlaylistSong ps WHERE ps.playlist.id = :playlistId AND ps.song.id = :songId")
    PlaylistSong findByPlaylistIdAndSongId(@Param("playlistId") Long playlistId, @Param("songId") Long songId);
    
    int countByPlaylistId(Long playlistId);
    PlaylistSong findByPlaylistIdAndSongId(Long playlistId, Long songId);
    List<PlaylistSong> findByPlaylistIdOrderByPositionAsc(Long playlistId);
}