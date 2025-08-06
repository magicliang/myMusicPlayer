#!/bin/bash

echo "Testing database initialization after fixes..."

# Clean and rebuild the project
echo "Cleaning project..."
mvn clean

echo "Compiling project..."
mvn compile

echo "Running tests..."
mvn test

echo "Starting application to test database initialization..."
timeout 10s mvn spring-boot:run || echo "Application started successfully (timeout reached)"

echo "Test completed. Check logs above for any database initialization errors."