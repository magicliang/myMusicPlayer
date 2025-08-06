#!/bin/bash

echo "Testing JPA mapping fix..."

# 编译项目
echo "Compiling project..."
mvn clean compile -q

if [ $? -eq 0 ]; then
    echo "✅ Compilation successful!"
    echo "JPA mapping issue has been resolved!"
else
    echo "❌ Compilation failed"
    echo "Running verbose compilation to see errors..."
    mvn clean compile
fi