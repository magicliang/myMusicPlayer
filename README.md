# 音乐播放器系统 (Music Player System)

基于Spring Boot和H2内存数据库构建的现代音乐播放器系统，实现了博客中描述的核心功能。

## 功能特性

### 核心功能

- ✅ 用户管理（注册、登录）
- ✅ 歌曲管理（CRUD操作）
- ✅ 播放列表管理（创建、编辑、添加/删除歌曲）
- ✅ 播放器控制（播放、暂停、上一首、下一首、进度控制）
- ✅ 实时状态同步（WebSocket支持多设备同步）
- ✅ 搜索功能（按歌曲名、艺术家搜索）

### 技术架构

- **后端**: Spring Boot 3.2.0
- **数据库**: H2内存数据库（无需外部配置）
- **实时通信**: WebSocket (STOMP协议)
- **前端**: 原生JavaScript + Tailwind CSS
- **构建工具**: Maven

## 快速开始

### 1. 运行项目

#### 方式一：直接运行

```bash
# 克隆项目
git clone [项目地址]
cd music-player

# 运行Spring Boot应用
./mvnw spring-boot:run
```

#### 方式二：使用Docker

```bash
# 构建镜像
docker build -t music-player .

# 运行容器
docker run -p 8080:8080 music-player
```

### 2. 访问应用

- 主应用: http://localhost:8080
- H2控制台: http://localhost:8080/h2-console
  - JDBC URL: jdbc:h2:mem:musicdb
  - 用户名: sa
  - 密码: password

### 3. API端点

#### 用户管理

- `GET /api/users/{userId}` - 获取用户信息
- `POST /api/users/register` - 注册用户

#### 歌曲管理
- `GET /api/songs` - 获取所有歌曲
- `GET /api/songs/{songId}` - 获取特定歌曲
- `GET /api/songs/search?query={keyword}` - 搜索歌曲

#### 播放列表
- `GET /api/playlists/user/{userId}` - 获取用户播放列表
- `POST /api/playlists` - 创建播放列表
- `POST /api/playlists/{playlistId}/songs/{songId}` - 添加歌曲到播放列表
- `DELETE /api/playlists/{playlistId}/songs/{songId}` - 从播放列表删除歌曲

#### 播放器控制
- `GET /api/player/state/{userId}` - 获取播放状态
- `POST /api/player/play/{userId}/{songId}` - 播放歌曲
- `POST /api/player/pause/{userId}` - 暂停播放
- `POST /api/player/resume/{userId}` - 恢复播放
- `POST /api/player/seek/{userId}?timestamp={seconds}` - 跳转到指定时间

### 4. WebSocket端点

- 连接: `ws://localhost:8080/ws`
- 订阅: `/user/{userId}/queue/player-state`
- 发送: `/app/player-event`

## 项目结构

```
music-player/
├── src/main/java/com/musicplayer/
│   ├── MusicPlayerApplication.java
│   ├── config/
│   │   ├── WebSocketConfig.java
│   │   └── DataInitializer.java
│   ├── controller/
│   │   ├── UserController.java
│   │   ├── SongController.java
│   │   ├── PlaylistController.java
│   │   ├── PlayerController.java
│   │   ├── ArtistController.java
│   │   └── AlbumController.java
│   ├── entity/
│   │   ├── User.java
│   │   ├── Artist.java
│   │   ├── Album.java
│   │   ├── Song.java
│   │   ├── Playlist.java
│   │   └── PlayerState.java
│   ├── repository/
│   │   ├── UserRepository.java
│   │   ├── SongRepository.java
│   │   ├── PlaylistRepository.java
│   │   ├── PlayerStateRepository.java
│   │   ├── ArtistRepository.java
│   │   └── AlbumRepository.java
│   └── service/
│       ├── MusicService.java
│       └── StateSyncService.java
├── src/main/resources/
│   ├── application.properties
│   └── static/
│       ├── index.html
│       └── app.js
├── pom.xml
├── Dockerfile
└── README.md
```

## 示例数据

项目启动时会自动创建以下示例数据：

### 用户

- john_doe (john@example.com)
- jane_smith (jane@example.com)

### 艺术家

- Taylor Swift
- Ed Sheeran
- Adele

### 专辑

- 1989 (Taylor's Version)
- ÷ (Divide)
- 25
- Midnights

### 歌曲

- Shake It Off
- Blank Space
- Shape of You
- Castle on the Hill
- Hello
- When We Were Young
- Anti-Hero
- Lavender Haze

### 播放列表

- My Favorites
- Workout Mix
- Chill Vibes

## 开发说明

### 数据库

使用H2内存数据库，数据在应用重启后会重置。如需持久化，可修改`application.properties`配置。

### 前端

前端使用原生JavaScript和Tailwind CSS构建，支持响应式设计。主要功能包括：

- 歌曲列表展示
- 播放器控制
- 播放列表管理
- 实时状态同步

### 扩展功能

- 用户认证和授权
- 文件上传（歌曲、封面图片）
- 推荐系统
- 社交功能（分享播放列表）
- 离线下载
- 音频质量选择

## 技术亮点

1. **实时同步**: 使用WebSocket实现多设备播放状态同步
2. **响应式设计**: 支持桌面和移动设备
3. **模块化架构**: 清晰的MVC分层结构
4. **内存数据库**: 零配置快速启动
5. **RESTful API**: 标准的API设计
6. **优雅降级**: WebSocket不可用时仍可正常使用

## 许可证

MIT License