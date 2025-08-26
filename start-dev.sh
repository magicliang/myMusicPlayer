#!/bin/bash

echo "Starting Music Player in Development Mode with H2 In-Memory Database..."

# Set development profile
export SPRING_PROFILES_ACTIVE=dev

# Start the application
mvn spring-boot:run -Dspring-boot.run.profiles=dev

echo "Application started!"
echo "H2 Console available at: http://localhost:8080/h2-console"
echo "JDBC URL: jdbc:h2:mem:musicdb"
echo "Username: sa"
echo "Password: (empty)"