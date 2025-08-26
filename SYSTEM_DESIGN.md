# Music Player System Design

## 1. Problem Statement

Design a scalable music streaming platform that allows users to:

- Browse and search for songs, artists, and albums
- Create and manage playlists
- Stream music with real-time player state synchronization
- Like/unlike songs
- Real-time player state sharing across multiple devices

## 2. Requirements

### Functional Requirements

- User registration and authentication
- Music catalog management (songs, artists, albums)
- Playlist creation and management
- Music streaming and playback control
- Search functionality
- Real-time player state synchronization
- Like/unlike songs functionality

### Non-Functional Requirements

- **Scalability**: Support 1M+ concurrent users
- **Availability**: 99.9% uptime
- **Latency**: < 100ms for API responses, < 50ms for real-time updates
- **Consistency**: Eventually consistent for non-critical data
- **Security**: Secure user authentication and data protection

## 3. Capacity Estimation

### Scale Assumptions

- 10M registered users
- 1M daily active users
- 100K concurrent users during peak hours
- 1M songs in catalog
- Average song size: 5MB
- Average user creates 10 playlists
- 100 songs per playlist on average

### Storage Requirements

- **Music Files**: 1M songs × 5MB = 5TB
- **Metadata**: ~100GB (songs, artists, albums, playlists)
- **User Data**: ~10GB (users, preferences, history)
- **Total**: ~5.2TB

### Bandwidth Requirements

- **Peak Concurrent Streams**: 100K users
- **Average Bitrate**: 320kbps
- **Peak Bandwidth**: 100K × 320kbps = 32Gbps

## 4. System APIs

### User Management

```
POST /api/users/register
POST /api/users/login
GET /api/users/{userId}
PUT /api/users/{userId}
```

### Music Catalog

```
GET /api/songs
GET /api/songs/{songId}
GET /api/songs/search?query={query}
GET /api/artists
GET /api/artists/{artistId}
GET /api/albums
GET /api/albums/{albumId}
```

### Playlist Management

```
GET /api/playlists
POST /api/playlists
GET /api/playlists/{playlistId}
PUT /api/playlists/{playlistId}
DELETE /api/playlists/{playlistId}
POST /api/playlists/{playlistId}/songs
DELETE /api/playlists/{playlistId}/songs/{songId}
```

### Player Control

```
POST /api/player/play
POST /api/player/pause
POST /api/player/seek
GET /api/player/state
WebSocket: /ws/player-state
```

## 5. High-Level Design

```
[Client Apps] → [Load Balancer] → [API Gateway] → [Microservices]
                                                      ↓
[CDN] ← [File Storage] ← [Music Service] → [Database Cluster]
                              ↓
                         [Cache Layer] → [Message Queue] → [Real-time Service]
```

### Core Components

#### 5.1 API Gateway

- Request routing and load balancing
- Authentication and authorization
- Rate limiting and throttling
- Request/response transformation

#### 5.2 Microservices Architecture

- **User Service**: User management and authentication
- **Music Service**: Catalog management and metadata
- **Playlist Service**: Playlist CRUD operations
- **Player Service**: Playback control and state management
- **Search Service**: Full-text search capabilities
- **Notification Service**: Real-time updates

#### 5.3 Data Storage

- **Primary Database**: PostgreSQL cluster for transactional data
- **Cache Layer**: Redis for session data and frequently accessed content
- **Search Engine**: Elasticsearch for full-text search
- **File Storage**: AWS S3/MinIO for music files
- **CDN**: CloudFront/CloudFlare for global content delivery

#### 5.4 Real-time Communication

- **WebSocket Connections**: For real-time player state sync
- **Message Queue**: Apache Kafka for event streaming
- **Push Notifications**: For mobile app notifications

## 6. Detailed Design

### 6.1 Database Schema

#### Users Table

```sql
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

#### Artists Table

```sql
CREATE TABLE artists (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    bio TEXT,
    image_url VARCHAR(500),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

#### Albums Table

```sql
CREATE TABLE albums (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    artist_id BIGINT REFERENCES artists(id),
    cover_image_url VARCHAR(500),
    release_date DATE,
    genre VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

#### Songs Table

```sql
CREATE TABLE songs (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    artist_id BIGINT REFERENCES artists(id),
    album_id BIGINT REFERENCES albums(id),
    duration INTEGER NOT NULL,
    audio_url VARCHAR(500) NOT NULL,
    cover_image_url VARCHAR(500),
    track_number INTEGER,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

#### Playlists Table

```sql
CREATE TABLE playlists (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    owner_id BIGINT REFERENCES users(id),
    is_public BOOLEAN DEFAULT false,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

#### Player State Table

```sql
CREATE TABLE player_states (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT REFERENCES users(id) UNIQUE,
    current_song_id BIGINT REFERENCES songs(id),
    current_timestamp INTEGER DEFAULT 0,
    status VARCHAR(20) DEFAULT 'STOPPED',
    queue_order TEXT,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

### 6.2 Caching Strategy

#### Cache Layers

1. **Browser Cache**: Static assets (CSS, JS, images)
2. **CDN Cache**: Music files and cover images
3. **Application Cache**:
    - Popular songs metadata (Redis, TTL: 1 hour)
    - User sessions (Redis, TTL: 24 hours)
    - Search results (Redis, TTL: 30 minutes)

#### Cache Patterns

- **Cache-Aside**: For user data and playlists
- **Write-Through**: For critical user preferences
- **Write-Behind**: For analytics and play counts

### 6.3 Real-time Architecture

#### WebSocket Implementation

```java
@Component
public class PlayerStateWebSocketHandler extends TextWebSocketHandler {
    
    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        // Subscribe user to their player state updates
        String userId = getUserIdFromSession(session);
        subscribeToPlayerUpdates(userId, session);
    }
    
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        // Handle player state changes
        PlayerStateUpdate update = parseMessage(message);
        broadcastPlayerStateUpdate(update);
    }
}
```

#### Event-Driven Updates

```java
@EventListener
public void handlePlayerStateChange(PlayerStateChangeEvent event) {
    // Broadcast to all user's connected devices
    webSocketService.broadcastToUser(
        event.getUserId(), 
        event.getPlayerState()
    );
    
    // Update cache
    cacheService.updatePlayerState(event.getUserId(), event.getPlayerState());
}
```

## 7. Scalability Considerations

### 7.1 Horizontal Scaling

- **Stateless Services**: All services designed to be stateless
- **Database Sharding**: Shard by user_id for user-specific data
- **Read Replicas**: Multiple read replicas for catalog data
- **Auto-scaling**: Kubernetes HPA based on CPU/memory metrics

### 7.2 Performance Optimizations

- **Connection Pooling**: HikariCP for database connections
- **Async Processing**: Non-blocking I/O for file operations
- **Batch Operations**: Bulk inserts for analytics data
- **Compression**: Gzip compression for API responses

### 7.3 Monitoring and Observability

- **Metrics**: Prometheus + Grafana
- **Logging**: ELK Stack (Elasticsearch, Logstash, Kibana)
- **Tracing**: Jaeger for distributed tracing
- **Health Checks**: Spring Boot Actuator endpoints

## 8. Security Considerations

### 8.1 Authentication & Authorization

- **JWT Tokens**: For stateless authentication
- **OAuth 2.0**: For third-party integrations
- **Role-Based Access**: Admin, User roles
- **Rate Limiting**: Prevent abuse and DDoS

### 8.2 Data Protection

- **Encryption**: TLS 1.3 for data in transit
- **Password Hashing**: bcrypt with salt
- **Input Validation**: Prevent SQL injection and XSS
- **CORS Configuration**: Restrict cross-origin requests

## 9. Deployment Architecture

### 9.1 Kubernetes Deployment

```yaml
# Current implementation includes:
- Namespace isolation
- ConfigMaps for configuration
- Secrets for sensitive data
- Persistent volumes for database
- Horizontal Pod Autoscaler
- Ingress for external access
- Health checks and probes
```

### 9.2 CI/CD Pipeline

```yaml
stages:
  - test: Run unit and integration tests
  - build: Build Docker image
  - security-scan: Vulnerability scanning
  - deploy-staging: Deploy to staging environment
  - integration-tests: Run end-to-end tests
  - deploy-production: Deploy to production
```

## 10. Future Enhancements

### 10.1 Advanced Features

- **Machine Learning**: Personalized recommendations
- **Social Features**: Follow artists, share playlists
- **Offline Mode**: Download songs for offline listening
- **High-Quality Audio**: Support for lossless audio formats

### 10.2 Technical Improvements

- **Microservices**: Break down into smaller services
- **Event Sourcing**: For audit trails and replay capability
- **CQRS**: Separate read and write models
- **GraphQL**: More flexible API queries

## 11. Conclusion

This music player system design provides a solid foundation for a scalable streaming platform. The current
implementation includes comprehensive testing, Kubernetes deployment, and follows best practices for security and
performance. The modular architecture allows for easy scaling and future enhancements while maintaining high
availability and user experience.