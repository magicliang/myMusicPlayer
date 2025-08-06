@GetMapping("/artist/{artistId}")
    public ResponseEntity<List<Album>> getAlbumsByArtist(@PathVariable Long artistId) {
        return ResponseEntity.ok(musicService.getAlbumsByArtist(artistId));
    }
    
    @GetMapping("/search")
    public ResponseEntity<List<Album>> searchAlbums(@RequestParam String query) {
        return ResponseEntity.ok(musicService.searchAlbums(query));
    }