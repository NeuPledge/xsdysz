#mvn clean package -Dmaven.test.skip=true
cd ./yudao-server
docker build -t debrief-server .

docker tag debrief-server:latest registry.cn-beijing.aliyuncs.com/simuhunluo/debrief-server:0321
docker push registry.cn-beijing.aliyuncs.com/simuhunluo/debrief-server:0321
# docker pull simuhunluo/debrief-server:1204
# 1
