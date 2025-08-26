#!/bin/bash

echo "Starting Music Player in Production Mode with PostgreSQL..."

# Set production profile
export SPRING_PROFILES_ACTIVE=production

# Check if required environment variables are set
if [ -z "$SPRING_DATASOURCE_URL" ] || [ -z "$SPRING_DATASOURCE_USERNAME" ] || [ -z "$SPRING_DATASOURCE_PASSWORD" ]; then
    echo "Error: Required environment variables not set:"
    echo "  SPRING_DATASOURCE_URL"
    echo "  SPRING_DATASOURCE_USERNAME" 
    echo "  SPRING_DATASOURCE_PASSWORD"
    exit 1
fi

# Start the application
mvn spring-boot:run -Dspring-boot.run.profiles=production

echo "Application started in production mode!"