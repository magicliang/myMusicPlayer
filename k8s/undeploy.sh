#!/bin/bash

# Undeploy Music Player from Kubernetes

echo "Undeploying Music Player from Kubernetes..."

# Delete HPA
kubectl delete -f hpa.yaml --ignore-not-found=true

# Delete ingress
kubectl delete -f ingress.yaml --ignore-not-found=true

# Delete application
kubectl delete -f music-player-deployment.yaml --ignore-not-found=true

# Delete PostgreSQL
kubectl delete -f postgres-deployment.yaml --ignore-not-found=true

# Delete PVC
kubectl delete -f postgres-pvc.yaml --ignore-not-found=true

# Delete configmaps and secrets
kubectl delete -f configmap.yaml --ignore-not-found=true
kubectl delete -f secret.yaml --ignore-not-found=true

# Delete namespace (this will delete everything in the namespace)
kubectl delete -f namespace.yaml --ignore-not-found=true

echo "Undeployment completed!"