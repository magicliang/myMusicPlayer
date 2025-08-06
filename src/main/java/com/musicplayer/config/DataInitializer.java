package com.musicplayer.config;

import com.musicplayer.entity.Album;
import com.musicplayer.entity.Artist;
import com.musicplayer.entity.Playlist;
import com.musicplayer.entity.User;
import com.musicplayer.service.MusicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private MusicService musicService;
    
    @Override
    public void run(String... args) throws Exception {
        initializeData();
    }

    private void initializeData() {
        // Create sample users
        User user1 = musicService.createUser("john_doe", "password123", "john@example.com");
        User user2 = musicService.createUser("jane_smith", "password123", "jane@example.com");

        // Create sample artists
        Artist artist1 = musicService.createArtist("Taylor Swift", "American singer-songwriter",
                "https://via.placeholder.com/300x300?text=Taylor+Swift");
        Artist artist2 = musicService.createArtist("Ed Sheeran", "English singer-songwriter",
                "https://via.placeholder.com/300x300?text=Ed+Sheeran");
        Artist artist3 = musicService.createArtist("Adele", "English singer-songwriter",
                "https://via.placeholder.com/300x300?text=Adele");
        
        // Create sample albums
        Album album1 = musicService.createAlbum("1989 (Taylor's Version)", artist1.getId(),
                "https://via.placeholder.com/300x300?text=1989+TV", "Pop");
        Album album2 = musicService.createAlbum("÷ (Divide)", artist2.getId(),
                "https://via.placeholder.com/300x300?text=Divide", "Pop");
        Album album3 = musicService.createAlbum("25", artist3.getId(), "https://via.placeholder.com/300x300?text=25",
                "Pop");
        Album album4 = musicService.createAlbum("Midnights", artist1.getId(),
                "https://via.placeholder.com/300x300?text=Midnights", "Pop");
        
        // Create sample songs
        musicService.createSong("Shake It Off", artist1.getId(), album1.getId(), 219,
                "https://www.soundjay.com/misc/sounds/bell-ringing-05.wav");
        musicService.createSong("Blank Space", artist1.getId(), album1.getId(), 231,
                "https://www.soundjay.com/misc/sounds/bell-ringing-05.wav");
        musicService.createSong("Shape of You", artist2.getId(), album2.getId(), 233,
                "https://www.soundjay.com/misc/sounds/bell-ringing-05.wav");
        musicService.createSong("Castle on the Hill", artist2.getId(), album2.getId(), 261,
                "https://www.soundjay.com/misc/sounds/bell-ringing-05.wav");
        musicService.createSong("Hello", artist3.getId(), album3.getId(), 295,
                "https://www.soundjay.com/misc/sounds/bell-ringing-05.wav");
        musicService.createSong("When We Were Young", artist3.getId(), album3.getId(), 290,
                "https://www.soundjay.com/misc/sounds/bell-ringing-05.wav");
        musicService.createSong("Anti-Hero", artist1.getId(), album4.getId(), 201,
                "https://www.soundjay.com/misc/sounds/bell-ringing-05.wav");
        musicService.createSong("Lavender Haze", artist1.getId(), album4.getId(), 202,
                "https://www.soundjay.com/misc/sounds/bell-ringing-05.wav");
        
        // Create sample playlists
        Playlist playlist1 = musicService.createPlaylist(user1.getId(), "My Favorites", "My favorite songs");
        Playlist playlist2 = musicService.createPlaylist(user1.getId(), "Workout Mix", "High energy songs for workout");
        Playlist playlist3 = musicService.createPlaylist(user2.getId(), "Chill Vibes",
                "Relaxing songs for chill moments");
        
        // Add songs to playlists
        musicService.addSongToPlaylist(playlist1.getId(), 1L);
        musicService.addSongToPlaylist(playlist1.getId(), 3L);
        musicService.addSongToPlaylist(playlist1.getId(), 5L);
        musicService.addSongToPlaylist(playlist1.getId(), 7L);

        musicService.addSongToPlaylist(playlist2.getId(), 1L);
        musicService.addSongToPlaylist(playlist2.getId(), 2L);
        musicService.addSongToPlaylist(playlist2.getId(), 3L);
        musicService.addSongToPlaylist(playlist2.getId(), 4L);

        musicService.addSongToPlaylist(playlist3.getId(), 5L);
        musicService.addSongToPlaylist(playlist3.getId(), 6L);
        musicService.addSongToPlaylist(playlist3.getId(), 7L);
        musicService.addSongToPlaylist(playlist3.getId(), 8L);

        System.out.println("Sample data initialized successfully!");
    }
}