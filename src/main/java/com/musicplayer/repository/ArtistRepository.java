package com.musicplayer.repository;

import com.musicplayer.entity.Artist;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArtistRepository extends JpaRepository<Artist, Long> {
    List<Artist> findByNameContainingIgnoreCase(String name);
}