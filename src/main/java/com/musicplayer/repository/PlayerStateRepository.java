package com.musicplayer.repository;

import com.musicplayer.entity.PlayerState;
import com.musicplayer.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerStateRepository extends JpaRepository<PlayerState, Long> {

    Optional<PlayerState> findByUser(User user);

    Optional<PlayerState> findByUserId(Long userId);
}