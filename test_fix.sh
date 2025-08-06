#!/bin/bash

echo "测试音乐播放器数据库修复..."
echo "================================"

# 清理之前的构建
echo "清理之前的构建..."
mvn clean

# 重新编译项目
echo "重新编译项目..."
mvn compile

# 运行应用测试
echo "运行应用测试..."
timeout 30s mvn spring-boot:run > app.log 2>&1 &

# 等待应用启动
echo "等待应用启动..."
sleep 15

# 检查应用是否成功启动
if pgrep -f "MusicPlayerApplication" > /dev/null; then
    echo "✅ 应用成功启动！"
    
    # 检查H2控制台是否可用
    if curl -s http://localhost:8080/h2-console | grep -q "H2 Console"; then
        echo "✅ H2控制台可访问: http://localhost:8080/h2-console"
    else
        echo "⚠️  H2控制台可能未完全就绪"
    fi
    
    # 检查主应用是否可访问
    if curl -s http://localhost:8080 | grep -q "Music Player"; then
        echo "✅ 主应用可访问: http://localhost:8080"
    else
        echo "⚠️  主应用可能未完全就绪"
    fi
    
    # 停止应用
    pkill -f "MusicPlayerApplication"
    sleep 3
    
else
    echo "❌ 应用启动失败，检查日志..."
    cat app.log | tail -50
fi

echo ""
echo "测试完成！"