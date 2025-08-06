@GetMapping("/{artistId}")
    public ResponseEntity<Artist> getArtist(@PathVariable Long artistId) {
        Artist artist = musicService.getArtistById(artistId);
        if (artist != null) {
            return ResponseEntity.ok(artist);
        }
        return ResponseEntity.notFound().build();
    }
    
    @PostMapping
    public ResponseEntity<Artist> createArtist(@RequestBody Artist artist) {
        Artist newArtist = musicService.createArtist(
            artist.getName(),
            artist.getBio(),
            artist.getImageUrl()
        );
        return ResponseEntity.ok(newArtist);
    }
    
    @GetMapping("/search")
    public ResponseEntity<List<Artist>> searchArtists(@RequestParam String query) {
        return ResponseEntity.ok(musicService.searchArtists(query));
    }