package com.musicplayer.config;

import com.musicplayer.entity.Album;
import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private AlbumRepository albumRepository;

    @Autowired
    private SongRepository songRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {
        // 检查是否已有数据
        if (artistRepository.count() > 0) {
            return;
        }

        // 创建艺术家
        Artist artist1 = new Artist();
        artist1.setName("周杰伦");
        artist1.setBio("华语流行乐天王，创作型歌手");
        artist1.setImageUrl("https://via.placeholder.com/300x300?text=周杰伦");
        artistRepository.save(artist1);

        Artist artist2 = new Artist();
        artist2.setName("Taylor Swift");
        artist2.setBio("American singer-songwriter");
        artist2.setImageUrl("https://via.placeholder.com/300x300?text=Taylor+Swift");
        artistRepository.save(artist2);

        // 创建专辑
        Album album1 = new Album();
        album1.setTitle("范特西");
        album1.setArtist(artist1);
        album1.setReleaseDate(LocalDate.of(2001, 9, 14));
        album1.setCoverUrl("https://via.placeholder.com/500x500?text=范特西");
        albumRepository.save(album1);

        Album album2 = new Album();
        album2.setTitle("1989");
        album2.setArtist(artist2);
        album2.setReleaseDate(LocalDate.of(2014, 10, 27));
        album2.setCoverUrl("https://via.placeholder.com/500x500?text=1989");
        albumRepository.save(album2);

        // 创建歌曲
        Song song1 = new Song();
        song1.setTitle("简单爱");
        song1.setDuration(258);
        song1.setAudioUrl("https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3");
        song1.setAlbum(album1);
        song1.setArtist(artist1);
        song1.setTrackNumber(1);
        songRepository.save(song1);

        Song song2 = new Song();
        song2.setTitle("开不了口");
        song2.setDuration(284);
        song2.setAudioUrl("https://www.soundhelix.com/examples/mp3/SoundHelix-Song-2.mp3");
        song2.setAlbum(album1);
        song2.setArtist(artist1);
        song2.setTrackNumber(2);
        songRepository.save(song2);

        Song song3 = new Song();
        song3.setTitle("Shake It Off");
        song3.setDuration(219);
        song3.setAudioUrl("https://www.soundhelix.com/examples/mp3/SoundHelix-Song-3.mp3");
        song3.setAlbum(album2);
        song3.setArtist(artist2);
        song3.setTrackNumber(1);
        songRepository.save(song3);

        Song song4 = new Song();
        song4.setTitle("Blank Space");
        song4.setDuration(231);
        song4.setAudioUrl("https://www.soundhelix.com/examples/mp3/SoundHelix-Song-4.mp3");
        song4.setAlbum(album2);
        song4.setArtist(artist2);
        song4.setTrackNumber(2);
        songRepository.save(song4);

        Song song5 = new Song();
        song5.setTitle("七里香");
        song5.setDuration(295);
        song5.setAudioUrl("https://www.soundhelix.com/examples/mp3/SoundHelix-Song-5.mp3");
        song5.setArtist(artist1);
        song5.setTrackNumber(1);
        songRepository.save(song5);

        System.out.println("示例数据初始化完成！");
    }
}
