package com.musicplayer.repository;

import com.musicplayer.entity.Playlist;
import com.musicplayer.entity.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlaylistRepository extends JpaRepository<Playlist, Long> {
    List<Playlist> findByOwnerId(Long ownerId);
    List<Playlist> findByNameContainingIgnoreCase(String name);

    List<Playlist> findByOwner(User owner);

    List<Playlist> findByIsPublicTrue();
}