// 全局变量
const API_BASE_URL = 'http://localhost:8080/api';
let currentUserId = 1; // 默认用户ID
let songs = [];
let playlists = [];
let currentSongIndex = -1;
let isPlaying = false;
let isShuffle = false;
let repeatMode = 'none'; // none, one, all

// DOM元素
const audioPlayer = document.getElementById('audioPlayer');
const songsList = document.getElementById('songsList');
const playlistsList = document.getElementById('playlistsList');
const currentSongTitle = document.getElementById('currentSongTitle');
const currentSongArtist = document.getElementById('currentSongArtist');
const currentTime = document.getElementById('currentTime');
const totalTime = document.getElementById('totalTime');
const progressBar = document.getElementById('progressBar');
const playPauseBtn = document.getElementById('playPauseBtn');
const prevBtn = document.getElementById('prevBtn');
const nextBtn = document.getElementById('nextBtn');
const shuffleBtn = document.getElementById('shuffleBtn');
const repeatBtn = document.getElementById('repeatBtn');
const refreshBtn = document.getElementById('refreshBtn');

// 初始化
document.addEventListener('DOMContentLoaded', function () {
    loadSongs();
    loadPlaylists();
    setupEventListeners();
});

// 设置事件监听器
function setupEventListeners() {
    playPauseBtn.addEventListener('click', togglePlayPause);
    prevBtn.addEventListener('click', playPrevious);
    nextBtn.addEventListener('click', playNext);
    shuffleBtn.addEventListener('click', toggleShuffle);
    repeatBtn.addEventListener('click', toggleRepeat);
    refreshBtn.addEventListener('click', refreshData);

  audioPlayer.addEventListener('timeupdate', updateProgress);
    audioPlayer.addEventListener('ended', handleSongEnd);
    audioPlayer.addEventListener('loadedmetadata', updateDuration);
}

// 加载歌曲列表
async function loadSongs() {
    try {
        const response = await fetch(`${API_BASE_URL}/songs`);
        const data = await response.json();
        if (data.success) {
            songs = data.data;
            renderSongs();
        }
    } catch (error) {
        console.error('加载歌曲失败:', error);
    }
}

// 加载播放列表
async function loadPlaylists() {
    try {
        const response = await fetch(`${API_BASE_URL}/playlists`);
        const data = await response.json();
        if (data.success) {
            playlists = data.data;
            renderPlaylists();
        }
    } catch (error) {
        console.error('加载播放列表失败:', error);
    }
}

// 渲染歌曲列表
function renderSongs() {
    songsList.innerHTML = '';
    songs.forEach((song, index) => {
        const songElement = createSongElement(song, index);
        songsList.appendChild(songElement);
    });
}

// 创建歌曲元素
function createSongElement(song, index) {
    const div = document.createElement('div');
    div.className = 'song-card bg-gray-50 rounded-lg p-4 flex items-center justify-between hover:bg-gray-100 cursor-pointer';
    div.innerHTML = `
        <div class="flex items-center space-x-4">
            <div class="w-12 h-12 bg-purple-600 rounded-lg flex items-center justify-center">
                <i class="fas fa-music text-white"></i>
            </div>
            <div>
                <h4 class="font-semibold">${song.title}</h4>
                <p class="text-sm text-gray-600">${song.artistName
    || '未知艺术家'}</p>
            </div>
        </div>
        <div class="flex items-center space-x-2">
            <span class="text-sm text-gray-500">${formatDuration(song.duration)}</span>
            <button class="play-song-btn p-2 text-purple-600 hover:text-purple-800" data-index="${index}">
                <i class="fas fa-play"></i>
            </button>
        </div>
    `;

  div.addEventListener('click', (e) => {
        if (!e.target.closest('.play-song-btn')) {
            playSong(index);
        }
    });

  div.querySelector('.play-song-btn').addEventListener('click', (e) => {
        e.stopPropagation();
        playSong(index);
    });

  return div;
}

// 渲染播放列表
function renderPlaylists() {
    playlistsList.innerHTML = '';
    playlists.forEach(playlist => {
        const div = document.createElement('div');
        div.className = 'p-3 bg-gray-50 rounded-lg hover:bg-gray-100 cursor-pointer';
        div.innerHTML = `
            <h4 class="font-medium">${playlist.name}</h4>
            <p class="text-sm text-gray-600">${playlist.description || '无描述'}</p>
        `;
        playlistsList.appendChild(div);
    });
}

// 播放歌曲
function playSong(index) {
    if (index >= 0 && index < songs.length) {
        currentSongIndex = index;
        const song = songs[index];

      audioPlayer.src = song.audioUrl;
        audioPlayer.play();

      isPlaying = true;
        updatePlayPauseButton();
        updateCurrentSongInfo(song);

      // 更新播放状态到服务器
        updatePlayerState(song.id, 0, true);
    }
}

// 更新当前歌曲信息
function updateCurrentSongInfo(song) {
    currentSongTitle.textContent = song.title;
    currentSongArtist.textContent = song.artistName || '未知艺术家';
}

// 切换播放/暂停
function togglePlayPause() {
    if (currentSongIndex === -1 && songs.length > 0) {
        playSong(0);
        return;
    }

  if (isPlaying) {
        audioPlayer.pause();
        isPlaying = false;
    updatePlayerState(songs[currentSongIndex].id, audioPlayer.currentTime,
        false);
    } else {
        audioPlayer.play();
        isPlaying = true;
    updatePlayerState(songs[currentSongIndex].id, audioPlayer.currentTime,
        true);
    }
    updatePlayPauseButton();
}

// 更新播放/暂停按钮
function updatePlayPauseButton() {
    const icon = playPauseBtn.querySelector('i');
    icon.className = isPlaying ? 'fas fa-pause' : 'fas fa-play';
}

// 播放上一首
function playPrevious() {
  if (songs.length === 0) {
    return;
  }

  let newIndex;
    if (isShuffle) {
        newIndex = Math.floor(Math.random() * songs.length);
    } else {
        newIndex = (currentSongIndex - 1 + songs.length) % songs.length;
    }
    playSong(newIndex);
}

// 播放下一首
function playNext() {
  if (songs.length === 0) {
    return;
  }

  let newIndex;
    if (isShuffle) {
        newIndex = Math.floor(Math.random() * songs.length);
    } else {
        newIndex = (currentSongIndex + 1) % songs.length;
    }
    playSong(newIndex);
}

// 切换随机播放
function toggleShuffle() {
    isShuffle = !isShuffle;
    shuffleBtn.classList.toggle('text-purple-600', isShuffle);
    shuffleBtn.classList.toggle('text-gray-600', !isShuffle);
}

// 切换重复模式
function toggleRepeat() {
    const modes = ['none', 'one', 'all'];
    const currentIndex = modes.indexOf(repeatMode);
    repeatMode = modes[(currentIndex + 1) % modes.length];

  const icon = repeatBtn.querySelector('i');
  repeatBtn.className = `p-2 ${repeatMode !== 'none' ? 'text-purple-600'
      : 'text-gray-600'} hover:text-purple-600`;
    
    if (repeatMode === 'one') {
        icon.className = 'fas fa-redo-alt';
    } else if (repeatMode === 'all') {
        icon.className = 'fas fa-redo';
    } else {
        icon.className = 'fas fa-redo';
    }
}

// 更新进度条
function updateProgress() {
    if (audioPlayer.duration) {
        const progress = (audioPlayer.currentTime / audioPlayer.duration) * 100;
        progressBar.style.width = progress + '%';
        currentTime.textContent = formatTime(audioPlayer.currentTime);
    }
}

// 更新总时长
function updateDuration() {
    totalTime.textContent = formatTime(audioPlayer.duration);
}

// 处理歌曲结束
function handleSongEnd() {
    if (repeatMode === 'one') {
        audioPlayer.currentTime = 0;
        audioPlayer.play();
    } else if (repeatMode === 'all' || currentSongIndex < songs.length - 1) {
        playNext();
    } else {
        isPlaying = false;
        updatePlayPauseButton();
    }
}

// 更新播放状态到服务器
async function updatePlayerState(songId, position, playing) {
    try {
        const playerState = {
            currentSongId: songId,
            playbackPosition: Math.floor(position),
            isPlaying: playing,
            repeatMode: repeatMode.toUpperCase(),
            shuffleMode: isShuffle
        };

      await fetch(`${API_BASE_URL}/player/state/${currentUserId}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(playerState)
        });
    } catch (error) {
        console.error('更新播放状态失败:', error);
    }
}

// 刷新数据
function refreshData() {
    loadSongs();
    loadPlaylists();
}

// 格式化时间
function formatTime(seconds) {
    const mins = Math.floor(seconds / 60);
    const secs = Math.floor(seconds % 60);
    return `${mins}:${secs.toString().padStart(2, '0')}`;
}

// 格式化时长
function formatDuration(seconds) {
    return formatTime(seconds);
}

// 错误处理
window.addEventListener('error', function (e) {
    console.error('应用错误:', e.error);
});