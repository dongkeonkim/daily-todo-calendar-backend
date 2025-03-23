#!/bin/sh

# Spring Boot 애플리케이션 실행
exec java -Xms512m -Xmx1024m -XX:+UseG1GC \
    -Dspring.profiles.active=${SPRING_PROFILES_ACTIVE} \
    -Dspring.config.location=classpath:/application.yml,optional:/app/config/application.yml \
    -jar app.jar