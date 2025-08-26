#!/bin/bash

# Deploy Music Player to Kubernetes

echo "Deploying Music Player to Kubernetes..."

# Create namespace
kubectl apply -f namespace.yaml

# Create secrets and configmaps
kubectl apply -f secret.yaml
kubectl apply -f configmap.yaml

# Create persistent volume claim
kubectl apply -f postgres-pvc.yaml

# Deploy PostgreSQL
kubectl apply -f postgres-deployment.yaml

# Wait for PostgreSQL to be ready
echo "Waiting for PostgreSQL to be ready..."
kubectl wait --for=condition=available --timeout=300s deployment/postgres-deployment -n music-player

# Deploy Music Player application
kubectl apply -f music-player-deployment.yaml

# Wait for application to be ready
echo "Waiting for Music Player to be ready..."
kubectl wait --for=condition=available --timeout=300s deployment/music-player-deployment -n music-player

# Create ingress
kubectl apply -f ingress.yaml

# Create HPA
kubectl apply -f hpa.yaml

echo "Deployment completed!"
echo "Application will be available at: http://music-player.local"
echo ""
echo "To check status:"
echo "kubectl get pods -n music-player"
echo "kubectl get services -n music-player"
echo "kubectl get ingress -n music-player"