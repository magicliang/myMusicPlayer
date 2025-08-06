package com.musicplayer.service;

import com.musicplayer.dto.SongDTO;
import com.musicplayer.entity.Album;
import com.musicplayer.entity.Artist;
import com.musicplayer.entity.Song;
import com.musicplayer.repository.AlbumRepository;
import com.musicplayer.repository.ArtistRepository;
import com.musicplayer.repository.SongRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SongService {

    @Autowired
    private SongRepository songRepository;

    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private AlbumRepository albumRepository;

    public Song createSong(SongDTO songDTO) {
        Song song = new Song();
        song.setTitle(songDTO.getTitle());
        song.setDuration(songDTO.getDuration());
        song.setAudioUrl(songDTO.getAudioUrl());
        song.setTrackNumber(songDTO.getTrackNumber());

        Artist artist = artistRepository.findById(songDTO.getArtistId())
                .orElseThrow(() -> new RuntimeException("Artist not found"));
        song.setArtist(artist);

        if (songDTO.getAlbumId() != null) {
            Album album = albumRepository.findById(songDTO.getAlbumId())
                    .orElseThrow(() -> new RuntimeException("Album not found"));
            song.setAlbum(album);
        }

        return songRepository.save(song);
    }

    public Optional<Song> getSongById(Long id) {
        return songRepository.findById(id);
    }

    public List<Song> getAllSongs() {
        return songRepository.findAll();
    }

    public List<Song> getSongsByArtist(Long artistId) {
        return songRepository.findByArtistId(artistId);
    }

    public List<Song> getSongsByAlbum(Long albumId) {
        return songRepository.findByAlbumId(albumId);
    }

    public List<Song> searchSongs(String query) {
        return songRepository.findByTitleContainingIgnoreCase(query);
    }

    public List<Song> searchSongsByArtist(String artistName) {
        return songRepository.searchByTitleOrArtist(artistName);
    }

    public Song updateSong(Long id, SongDTO songDTO) {
        Song song = songRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Song not found"));

        song.setTitle(songDTO.getTitle());
        song.setDuration(songDTO.getDuration());
        song.setAudioUrl(songDTO.getAudioUrl());
        song.setTrackNumber(songDTO.getTrackNumber());

        Artist artist = artistRepository.findById(songDTO.getArtistId())
                .orElseThrow(() -> new RuntimeException("Artist not found"));
        song.setArtist(artist);

        if (songDTO.getAlbumId() != null) {
            Album album = albumRepository.findById(songDTO.getAlbumId())
                    .orElseThrow(() -> new RuntimeException("Album not found"));
            song.setAlbum(album);
        } else {
            song.setAlbum(null);
        }

        return songRepository.save(song);
    }

    public void deleteSong(Long id) {
        if (!songRepository.existsById(id)) {
            throw new RuntimeException("Song not found");
        }
        songRepository.deleteById(id);
    }

    public List<SongDTO> convertToDTOList(List<Song> songs) {
        return songs.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public SongDTO convertToDTO(Song song) {
        SongDTO dto = new SongDTO();
        dto.setId(song.getId());
        dto.setTitle(song.getTitle());
        dto.setDuration(song.getDuration());
        dto.setAudioUrl(song.getAudioUrl());
        dto.setTrackNumber(song.getTrackNumber());

        if (song.getArtist() != null) {
            dto.setArtistId(song.getArtist().getId());
            dto.setArtistName(song.getArtist().getName());
        }

        if (song.getAlbum() != null) {
            dto.setAlbumId(song.getAlbum().getId());
            dto.setAlbumTitle(song.getAlbum().getTitle());
        }

        return dto;
    }
}