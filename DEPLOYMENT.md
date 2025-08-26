# Music Player Deployment Guide

## Prerequisites

- Docker and Docker Compose
- Kubernetes cluster (minikube, kind, or cloud provider)
- kubectl configured
- Maven 3.6+
- Java 17+

## Local Development

### 1. Run with H2 In-Memory Database (Default/Development)

```bash
# Default profile uses H2 in-memory database
mvn spring-boot:run

# Or explicitly use dev profile
./start-dev.sh
# or
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

**H2 Console Access:**

- URL: http://localhost:8080/h2-console
- JDBC URL: `jdbc:h2:mem:musicdb`
- Username: `sa`
- Password: (empty)

### 2. Run with PostgreSQL (Production Mode)

```bash
# Set environment variables (if not using external PostgreSQL)
export SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/musicdb
export SPRING_DATASOURCE_USERNAME=musicuser
export SPRING_DATASOURCE_PASSWORD=musicpass

# Run with production profile
./start-prod.sh
# or
mvn spring-boot:run -Dspring-boot.run.profiles=production
```

### 3. Run Tests

```bash
# Run all tests (uses H2 in-memory database)
mvn test

# Run specific test categories
mvn test -Dtest="**/*Test"
mvn test -Dtest="**/*IntegrationTest"

# Tests automatically use H2 in-memory database via test profile
```

### 3. Build Docker Image

```bash
docker build -t music-player:latest .
```

## Kubernetes Deployment

### 1. Deploy to Kubernetes

```bash
cd k8s
./deploy.sh
```

### 2. Verify Deployment

```bash
kubectl get pods -n music-player
kubectl get services -n music-player
kubectl get ingress -n music-player
```

### 3. Access Application

Add to `/etc/hosts`:

```
127.0.0.1 music-player.local
```

Access at: http://music-player.local

### 4. Monitor Application

```bash
# Check logs
kubectl logs -f deployment/music-player-deployment -n music-player

# Check health
kubectl get pods -n music-player
kubectl describe pod <pod-name> -n music-player
```

### 5. Scale Application

```bash
kubectl scale deployment music-player-deployment --replicas=5 -n music-player
```

### 6. Undeploy

```bash
./undeploy.sh
```

## Production Considerations

### Environment Variables

- `SPRING_DATASOURCE_URL`
- `SPRING_DATASOURCE_USERNAME`
- `SPRING_DATASOURCE_PASSWORD`
- `ADMIN_PASSWORD`

### Resource Requirements

- **Minimum**: 512Mi memory, 250m CPU
- **Recommended**: 1Gi memory, 500m CPU
- **Database**: 5Gi storage for PostgreSQL

### Monitoring

- Health checks: `/actuator/health`
- Metrics: `/actuator/metrics`
- Prometheus: `/actuator/prometheus`