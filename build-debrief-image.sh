mvn clean package -Dmaven.test.skip=true
cd ./yudao-server
docker build -t debrief-server .

# docker tag debrief-server:latest simuhunluo/debrief-server:1204
# docker push simuhunluo/debrief-server:1204
# docker pull simuhunluo/debrief-server:1204
#
