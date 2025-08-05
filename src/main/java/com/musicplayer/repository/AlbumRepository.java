package com.musicplayer.repository;

import com.musicplayer.entity.Album;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlbumRepository extends JpaRepository<Album, Long> {

    List<Album> findByArtistId(Long artistId);

    List<Album> findByTitleContainingIgnoreCase(String title);
}