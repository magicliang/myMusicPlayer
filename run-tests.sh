#!/bin/bash

echo "Running Music Player Test Suite..."

# Set test profile
export SPRING_PROFILES_ACTIVE=test

# Run unit tests
echo "Running Unit Tests..."
mvn test -Dtest="com.musicplayer.entity.*Test,com.musicplayer.service.*Test,com.musicplayer.repository.*Test"

# Run integration tests
echo "Running Integration Tests..."
mvn test -Dtest="com.musicplayer.integration.*Test"

# Run controller tests
echo "Running Controller Tests..."
mvn test -Dtest="com.musicplayer.controller.*Test"

# Run application context test
echo "Running Application Context Test..."
mvn test -Dtest="com.musicplayer.MusicPlayerApplicationTest"

# Generate test report
echo "Generating Test Report..."
mvn surefire-report:report

echo "Test suite completed!"
echo "Test reports available in target/site/surefire-report.html"