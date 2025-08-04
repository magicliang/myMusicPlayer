# 音乐播放器后端系统

基于Spring Boot构建的音乐播放器后端API系统，提供完整的音乐播放、用户管理、播放列表管理等功能。

## 功能特性

- **用户管理**: 用户注册、登录、个人信息管理
- **音乐管理**: 歌曲、专辑、艺术家信息管理
- **播放列表**: 创建、编辑、分享播放列表
- **播放器控制**: 播放、暂停、上一首、下一首、进度控制
- **实时同步**: 多设备播放状态同步
- **搜索功能**: 按歌曲、艺术家、专辑搜索

## 技术栈

- **后端框架**: Spring Boot 3.2.0
- **数据库**: MySQL 8.0
- **ORM**: Spring Data JPA
- **安全**: Spring Security + JWT
- **构建工具**: Maven
- **容器化**: Docker

## 项目结构

```
src/main/java/com/musicplayer/
├── config/          # 配置类
├── controller/      # REST控制器
├── dto/            # 数据传输对象
├── entity/         # 实体类
├── repository/     # 数据访问层
└── service/        # 业务逻辑层
```

## API端点

### 用户管理

- `POST /api/users` - 创建用户
- `GET /api/users` - 获取所有用户
- `GET /api/users/{id}` - 获取指定用户
- `PUT /api/users/{id}` - 更新用户信息
- `DELETE /api/users/{id}` - 删除用户

### 歌曲管理

- `POST /api/songs` - 创建歌曲
- `GET /api/songs` - 获取所有歌曲
- `GET /api/songs/{id}` - 获取指定歌曲
- `GET /api/songs/artist/{artistId}` - 获取艺术家歌曲
- `GET /api/songs/album/{albumId}` - 获取专辑歌曲
- `GET /api/songs/search?query={query}` - 搜索歌曲
- `PUT /api/songs/{id}` - 更新歌曲信息
- `DELETE /api/songs/{id}` - 删除歌曲

### 播放列表管理

- `POST /api/playlists` - 创建播放列表
- `GET /api/playlists` - 获取公开播放列表
- `GET /api/playlists/user/{userId}` - 获取用户播放列表
- `GET /api/playlists/{id}` - 获取播放列表详情
- `PUT /api/playlists/{id}` - 更新播放列表
- `DELETE /api/playlists/{id}` - 删除播放列表
- `POST /api/playlists/{playlistId}/songs/{songId}` - 添加歌曲到播放列表
- `DELETE /api/playlists/{playlistId}/songs/{songId}` - 从播放列表移除歌曲
- `GET /api/playlists/{playlistId}/songs` - 获取播放列表歌曲

### 播放器控制

- `GET /api/player/state/{userId}` - 获取播放状态
- `PUT /api/player/state/{userId}` - 更新播放状态
- `POST /api/player/play/{userId}/song/{songId}` - 播放指定歌曲
- `POST /api/player/pause/{userId}` - 暂停播放
- `POST /api/player/resume/{userId}` - 恢复播放
- `POST /api/player/seek/{userId}/{position}` - 跳转到指定位置

## 快速开始

### 环境要求

- Java 17+
- MySQL 8.0+
- Maven 3.6+

### 本地运行

1. 克隆项目

```bash
git clone <repository-url>
cd music-player-backend
```

2. 配置数据库
   确保MySQL已安装并运行，创建数据库或使用已创建的数据库。

3. 配置环境变量

```bash
export DB_HOST=localhost
export DB_PORT=3306
export DB_NAME=so2vc2j5
export DB_USERNAME=your_username
export DB_PASSWORD=your_password
```

4. 运行项目

```bash
mvn spring-boot:run
```

### Docker运行

1. 构建镜像

```bash
docker build -t music-player-backend .
```

2. 运行容器

```bash
docker run -p 8080:8080 \
  -e DB_HOST=your_mysql_host \
  -e DB_PORT=3306 \
  -e DB_NAME=so2vc2j5 \
  -e DB_USERNAME=your_username \
  -e DB_PASSWORD=your_password \
  music-player-backend
```

## 数据库配置

数据库连接配置通过环境变量设置：

- `DB_HOST`: 数据库主机地址
- `DB_PORT`: 数据库端口
- `DB_NAME`: 数据库名称
- `DB_USERNAME`: 数据库用户名
- `DB_PASSWORD`: 数据库密码

## 示例数据

项目启动时会自动创建示例数据，包括：

- 2位艺术家：周杰伦、Taylor Swift
- 2张专辑：范特西、1989
- 5首示例歌曲

## 开发计划

- [ ] 用户认证和授权
- [ ] 文件上传功能
- [ ] 推荐算法
- [ ] WebSocket实时通信
- [ ] 缓存优化
- [ ] 日志监控

## 贡献指南

欢迎提交Issue和Pull Request！

## 许可证

MIT License
