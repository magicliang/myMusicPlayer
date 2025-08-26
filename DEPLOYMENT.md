# Music Player Deployment Guide

## Prerequisites

- Docker and Docker Compose
- Kubernetes cluster (minikube, kind, or cloud provider)
- kubectl configured
- Maven 3.6+
- Java 17+

## Local Development

### 1. Run with H2 Database

```bash
mvn spring-boot:run
```

### 2. Run Tests

```bash
# Run all tests
mvn test

# Run specific test categories
mvn test -Dtest="**/*Test"
mvn test -Dtest="**/*IntegrationTest"
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