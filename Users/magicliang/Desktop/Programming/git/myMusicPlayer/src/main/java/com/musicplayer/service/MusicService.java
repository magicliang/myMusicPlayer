public List<Album> getAlbumsByArtist(Long artistId) {
        return albumRepository.findByArtistId(artistId);
    }
    
    public List<Album> searchAlbums(String query) {
        return albumRepository.findByTitleContainingIgnoreCase(query);
    }
    
    // Artist operations
    public List<Artist> getAllArtists() {
        return artistRepository.findAll();
    }
    
    public Artist getArtistById(Long artistId) {
        return artistRepository.findById(artistId).orElse(null);
    }
    
    public List<Artist> searchArtists(String query) {
        return artistRepository.findByNameContainingIgnoreCase(query);
    }
    
    // Playlist operations
    public List<Playlist> getUserPlaylists(Long userId) {
        return playlistRepository.findByOwnerId(userId);
    }
    
    public Playlist getPlaylistById(Long playlistId) {
        return playlistRepository.findById(playlistId).orElse(null);
    }
    
    @Autowired
    private PlaylistSongRepository playlistSongRepository;
    
    public Playlist addSongToPlaylist(Long playlistId, Long songId) {
        Playlist playlist = playlistRepository.findById(playlistId).orElse(null);
        Song song = songRepository.findById(songId).orElse(null);
        
        if (playlist == null || song == null) return null;
        
        // 计算新的位置（当前歌曲数量作为新位置）
        int newPosition = playlistSongRepository.countByPlaylistId(playlistId);
        
        PlaylistSong playlistSong = new PlaylistSong();
        playlistSong.setPlaylist(playlist);
        playlistSong.setSong(song);
        playlistSong.setPosition(newPosition);
        
        playlistSongRepository.save(playlistSong);
        return playlist;
    }
    
    public Playlist removeSongFromPlaylist(Long playlistId, Long songId) {
        Playlist playlist = playlistRepository.findById(playlistId).orElse(null);
        if (playlist == null) return null;
        
        // 找到对应的PlaylistSong并删除
        PlaylistSong playlistSong = playlistSongRepository.findByPlaylistIdAndSongId(playlistId, songId);
        if (playlistSong != null) {
            playlistSongRepository.delete(playlistSong);
            
            // 重新排序剩余的歌曲
            List<PlaylistSong> remainingSongs = playlistSongRepository.findByPlaylistIdOrderByPositionAsc(playlistId);
            for (int i = 0; i < remainingSongs.size(); i++) {
                PlaylistSong ps = remainingSongs.get(i);
                ps.setPosition(i);
                playlistSongRepository.save(ps);
            }
        }
        
        return playlist;
    }
    
    // Player state operations
    public List<Album> searchAlbums(String query) {
        return albumRepository.findByTitleContainingIgnoreCase(query);
    }
    
    public Playlist getPlaylistById(Long playlistId) {
        return playlistRepository.findById(playlistId).orElse(null);
    }
    
    public Artist getArtistById(Long artistId) {
        return artistRepository.findById(artistId).orElse(null);
    }
    
    public List<Artist> searchArtists(String query) {
        return artistRepository.findByNameContainingIgnoreCase(query);
    }
}