package com.musicplayer.repository;

import com.musicplayer.entity.Song;
import com.musicplayer.entity.User;
import com.musicplayer.entity.UserLikedSong;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserLikedSongRepository extends JpaRepository<UserLikedSong, Long> {

    List<UserLikedSong> findByUser(User user);

    Optional<UserLikedSong> findByUserAndSong(User user, Song song);

    boolean existsByUserAndSong(User user, Song song);

    void deleteByUserAndSong(User user, Song song);
}
