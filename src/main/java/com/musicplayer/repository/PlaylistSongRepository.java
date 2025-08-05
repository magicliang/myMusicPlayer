package com.musicplayer.repository;

import com.musicplayer.entity.Playlist;
import com.musicplayer.entity.PlaylistSong;
import com.musicplayer.entity.Song;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlaylistSongRepository extends JpaRepository<PlaylistSong, Long> {

    List<PlaylistSong> findByPlaylistOrderByPosition(Playlist playlist);

    Optional<PlaylistSong> findByPlaylistAndSong(Playlist playlist, Song song);

    boolean existsByPlaylistAndSong(Playlist playlist, Song song);

    void deleteByPlaylistAndSong(Playlist playlist, Song song);

    List<PlaylistSong> findByPlaylist(Playlist playlist);
}