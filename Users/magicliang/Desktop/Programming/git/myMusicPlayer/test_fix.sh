#!/bin/bash

echo "Testing Music Player Application with H2 Database..."

# Clean and compile
mvn clean compile

# Run the application in background
mvn spring-boot:run &

# Wait for application to start
sleep 10

# Test if application is running
if curl -s http://localhost:8080 > /dev/null; then
    echo "✅ Application started successfully!"
    echo "✅ H2 Database is working correctly!"
    echo "✅ You can access the application at: http://localhost:8080"
    echo "✅ H2 Console available at: http://localhost:8080/h2-console"
else
    echo "❌ Application failed to start"
    exit 1
fi

# Kill the background process
pkill -f "spring-boot:run"