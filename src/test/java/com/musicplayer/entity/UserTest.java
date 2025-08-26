package com.musicplayer.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserTest {

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setPassword("password123");
        user.setPlaylists(new ArrayList<>());
        user.setLikedSongs(new ArrayList<>());
    }

    @Test
    void testUserCreation() {
        assertNotNull(user);
        assertEquals(1L, user.getId());
        assertEquals("testuser", user.getUsername());
        assertEquals("test@example.com", user.getEmail());
        assertEquals("password123", user.getPassword());
        assertNotNull(user.getPlaylists());
        assertNotNull(user.getLikedSongs());
    }

    @Test
    void testUserEquality() {
        User user2 = new User();
        user2.setId(1L);
        user2.setUsername("testuser");
        user2.setEmail("test@example.com");
        user2.setPassword("password123");

        assertEquals(user, user2);
        assertEquals(user.hashCode(), user2.hashCode());
    }

    @Test
    void testAddPlaylist() {
        Playlist playlist = new Playlist();
        playlist.setId(1L);
        playlist.setName("My Playlist");
        playlist.setOwner(user);

        user.getPlaylists().add(playlist);

        assertEquals(1, user.getPlaylists().size());
        assertEquals(playlist, user.getPlaylists().get(0));
    }
}