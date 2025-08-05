// Global variables
let currentUserId = 1; // Default user for demo
let currentSong = null;
let isPlaying = false;
let currentPlaylist = [];
let currentIndex = 0;
let stompClient = null;

// Initialize application
document.addEventListener('DOMContentLoaded', function () {
  initializeApp();
  connectWebSocket();
});

function initializeApp() {
  loadSongs();
  loadPlaylists();
  loadPlayerState();
  setupEventListeners();
}

// API Functions
async function apiCall(endpoint, method = 'GET', data = null) {
  const options = {
    method: method,
    headers: {
      'Content-Type': 'application/json',
    }
  };
  if (data) {
    options.body = JSON.stringify(data);
  }

  const response = await fetch(`http://localhost:8080${endpoint}`, options);
  return response.json();
}

async function loadSongs() {
  try {
    const songs = await apiCall('/api/songs');
    displaySongs(songs);
  } catch (error) {
    console.error('Error loading songs:', error);
  }
}

async function loadPlaylists() {
  try {
    const playlists = await apiCall(`/api/playlists/user/${currentUserId}`);
    displayPlaylists(playlists);
  } catch (error) {
    console.error('Error loading playlists:', error);
  }
}

async function loadPlayerState() {
  try {
    const state = await apiCall(`/api/player/state/${currentUserId}`);
    if (state && state.currentSong) {
      currentSong = state.currentSong;
      updateNowPlaying(state.currentSong);
      updateProgress(state.currentTimestamp || 0);
      if (state.status === 'PLAYING') {
        playAudio();
      }
    }
  } catch (error) {
    console.error('Error loading player state:', error);
  }
}

// Display Functions
function displaySongs(songs) {
  const container = document.getElementById('songsList');
  container.innerHTML = '';

  songs.forEach(song => {
    const songDiv = document.createElement('div');
    songDiv.className = 'song-card bg-white/10 rounded-lg p-4 flex items-center space-x-4 hover:bg-white/20 cursor-pointer';
    songDiv.innerHTML = `
            <div class="w-12 h-12 bg-gray-700 rounded flex items-center justify-center">
                <i class="fas fa-music text-gray-400"></i>
            </div>
            <div class="flex-1">
                <h4 class="font-semibold">${song.title}</h4>
                <p class="text-sm text-gray-400">${song.artist.name}</p>
            </div>
            <div class="text-sm text-gray-400">
                ${formatDuration(song.duration)}
            </div>
            <button class="play-song-btn p-2 rounded-full hover:bg-white/20" data-song-id="${song.id}">
                <i class="fas fa-play"></i>
            </button>
        `;

    songDiv.addEventListener('click', (e) => {
      if (!e.target.classList.contains('play-song-btn')
          && !e.target.parentElement.classList.contains('play-song-btn')) {
        playSong(song);
      }
    });

    container.appendChild(songDiv);
  });

  // Add event listeners to play buttons
  document.querySelectorAll('.play-song-btn').forEach(btn => {
    btn.addEventListener('click', (e) => {
      e.stopPropagation();
      const songId = btn.getAttribute('data-song-id');
      playSongById(songId);
    });
  });
}

function displayPlaylists(playlists) {
  const container = document.getElementById('playlistsList');
  container.innerHTML = '';

  playlists.forEach(playlist => {
    const playlistDiv = document.createElement('div');
    playlistDiv.className = 'bg-white/10 rounded p-3 hover:bg-white/20 cursor-pointer';
    playlistDiv.innerHTML = `
            <h4 class="font-semibold">${playlist.name}</h4>
            <p class="text-sm text-gray-400">${playlist.songs.length} 首歌曲</p>
        `;

    playlistDiv.addEventListener('click', () => {
      loadPlaylistSongs(playlist.id);
    });

    container.appendChild(playlistDiv);
  });
}

function updateNowPlaying(song) {
  document.getElementById('currentSongTitle').textContent = song.title;
  document.getElementById('currentSongArtist').textContent = song.artist.name;

  const audio = document.getElementById('audioPlayer');
  audio.src = song.audioUrl;

  // Update duration display
  audio.addEventListener('loadedmetadata', () => {
    document.getElementById('totalTime').textContent = formatDuration(
        Math.floor(audio.duration));
  });
}

function updateProgress(currentTime) {
  const audio = document.getElementById('audioPlayer');
  const progressBar = document.getElementById('progressBar');

  if (audio.duration) {
    const percentage = (currentTime / audio.duration) * 100;
    progressBar.value = percentage;
  }

  document.getElementById('currentTime').textContent = formatDuration(
      currentTime);
}

// Player Functions
async function playSong(song) {
  currentSong = song;
  updateNowPlaying(song);

  try {
    await apiCall(`/api/player/play/${currentUserId}/${song.id}`, 'POST');
  } catch (error) {
    console.error('Error updating player state:', error);
  }

  playAudio();
}

async function playSongById(songId) {
  try {
    const song = await apiCall(`/api/songs/${songId}`);
    if (song) {
      playSong(song);
    }
  } catch (error) {
    console.error('Error loading song:', error);
  }
}

function playAudio() {
  const audio = document.getElementById('audioPlayer');
  const playBtn = document.getElementById('playPauseBtn');

  audio.play().then(() => {
    isPlaying = true;
    playBtn.innerHTML = '<i class="fas fa-pause"></i>';
  }).catch(error => {
    console.error('Error playing audio:', error);
  });
}

function pauseAudio() {
  const audio = document.getElementById('audioPlayer');
  const playBtn = document.getElementById('playPauseBtn');

  audio.pause();
  isPlaying = false;
  playBtn.innerHTML = '<i class="fas fa-play"></i>';

  apiCall(`/api/player/pause/${currentUserId}`, 'POST');
}

function togglePlayPause() {
  if (isPlaying) {
    pauseAudio();
  } else if (currentSong) {
    playAudio();
    apiCall(`/api/player/resume/${currentUserId}`, 'POST');
  }
}

function seekAudio() {
  const audio = document.getElementById('audioPlayer');
  const progressBar = document.getElementById('progressBar');

  const seekTime = (progressBar.value / 100) * audio.duration;
  audio.currentTime = seekTime;

  apiCall(`/api/player/seek/${currentUserId}?timestamp=${Math.floor(seekTime)}`,
      'POST');
}

// WebSocket Functions
function connectWebSocket() {
  const socket = new SockJS('http://localhost:8080/ws');
  stompClient = Stomp.over(socket);

  stompClient.connect({}, function (frame) {
    console.log('Connected: ' + frame);

    stompClient.subscribe(`/user/${currentUserId}/queue/player-state`,
        function (message) {
          const state = JSON.parse(message.body);
          handleStateUpdate(state);
        });
  });
}

function handleStateUpdate(state) {
  if (state.currentSong) {
    currentSong = state.currentSong;
    updateNowPlaying(state.currentSong);
    updateProgress(state.currentTimestamp || 0);

    if (state.status === 'PLAYING') {
      playAudio();
    } else {
      pauseAudio();
    }
  }
}

// Event Listeners
function setupEventListeners() {
  // Play/Pause button
  document.getElementById('playPauseBtn').addEventListener('click',
      togglePlayPause);

  // Progress bar
  document.getElementById('progressBar').addEventListener('input', seekAudio);

  // Audio time update
  document.getElementById('audioPlayer').addEventListener('timeupdate', (e) => {
    updateProgress(e.target.currentTime);
  });

  // Search functionality
  document.getElementById('searchBtn').addEventListener('click', () => {
    const searchInput = document.getElementById('searchInput');
    searchInput.classList.toggle('hidden');
    if (!searchInput.classList.contains('hidden')) {
      searchInput.focus();
    }
  });

  document.getElementById('searchInput').addEventListener('input',
      async (e) => {
        const query = e.target.value;
        if (query.length > 2) {
          try {
            const songs = await apiCall(
                `/api/songs/search?query=${encodeURIComponent(query)}`);
            displaySongs(songs);
          } catch (error) {
            console.error('Error searching songs:', error);
          }
        } else if (query.length === 0) {
          loadSongs();
        }
      });

  // Playlist modal
  document.getElementById('createPlaylistBtn').addEventListener('click', () => {
    document.getElementById('createPlaylistModal').classList.remove('hidden');
  });

  document.getElementById('cancelPlaylistBtn').addEventListener('click', () => {
    document.getElementById('createPlaylistModal').classList.add('hidden');
  });

  document.getElementById('savePlaylistBtn').addEventListener('click',
      async () => {
        const name = document.getElementById('playlistName').value;
        const description = document.getElementById(
            'playlistDescription').value;

        if (name) {
          try {
            await apiCall('/api/playlists', 'POST', {
              name: name,
              description: description,
              owner: {id: currentUserId}
            });
            loadPlaylists();
            document.getElementById('createPlaylistModal').classList.add(
                'hidden');
            document.getElementById('playlistName').value = '';
            document.getElementById('playlistDescription').value = '';
          } catch (error) {
            console.error('Error creating playlist:', error);
          }
        }
      });

  // Next/Previous buttons
  document.getElementById('nextBtn').addEventListener('click', () => {
    // Implement next song logic
    console.log('Next song');
  });

  document.getElementById('prevBtn').addEventListener('click', () => {
    // Implement previous song logic
    console.log('Previous song');
  });
}

// Utility Functions
function formatDuration(seconds) {
  const minutes = Math.floor(seconds / 60);
  const remainingSeconds = Math.floor(seconds % 60);
  return `${minutes}:${remainingSeconds.toString().padStart(2, '0')}`;
}

async function loadPlaylistSongs(playlistId) {
  try {
    const playlist = await apiCall(`/api/playlists/${playlistId}`);
    if (playlist && playlist.songs) {
      displaySongs(playlist.songs);
    }
  } catch (error) {
    console.error('Error loading playlist songs:', error);
  }
}