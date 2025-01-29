#!/bin/sh

# sshd 시작 시도를 로깅
echo "Starting sshd..."
sudo /usr/sbin/sshd
echo "sshd start attempted"

# DDNS 업데이트 스크립트를 백그라운드로 실행
echo "Starting DDNS update process..."
(while true; do /app/ddns-update.sh; sleep 1800; done) &
echo "DDNS update process started"

# Spring Boot 애플리케이션 실행
exec java -Xms512m -Xmx1024m -XX:+UseG1GC \
    -Dspring.profiles.active=${SPRING_PROFILES_ACTIVE} \
    -Dspring.config.location=classpath:/application.yml,optional:/app/config/application.yml \
    -jar app.jar