package com.musicplayer.repository;

import com.musicplayer.entity.Song;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SongRepository extends JpaRepository<Song, Long> {
    List<Song> findByArtistId(Long artistId);
    List<Song> findByAlbumId(Long albumId);
    List<Song> findByTitleContainingIgnoreCase(String title);
    // 这些方法通过其他方式实现
    
    @Query("SELECT s FROM Song s WHERE LOWER(s.title) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(s.artist.name) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<Song> searchByTitleOrArtist(@Param("query") String query);
}