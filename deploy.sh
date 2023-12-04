#!/bin/bash
set -e

## 第一步：删除可能启动的老 debrief-server 容器
echo "开始删除 debrief-server 容器"
docker stop debrief-server || true
docker rm debrief-server || true
echo "完成删除 debrief-server 容器"

## 第二步：启动新的 debrief-server 容器 \
echo "开始启动 debrief-server 容器"
docker run -d \
--name debrief-server \
-p 48080:48080 \
-e "SPRING_PROFILES_ACTIVE=dev" \
-v /work/projects/debrief-server:/root/logs/ \
simuhunluo/debrief-server:1204
echo "正在启动 debrief-server 容器中，需要等待 60 秒左右"
