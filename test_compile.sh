#!/bin/bash
echo "开始编译项目..."
cd /myMusicPlayer
mvn clean compile -q
if [ $? -eq 0 ]; then
    echo "✅ 编译成功！"
else
    echo "❌ 编译失败！"
    mvn clean compile
fi