# Build 단계
FROM --platform=linux/amd64 eclipse-temurin:17-jdk AS builder

WORKDIR /app

COPY gradlew settings.gradle build.gradle ./
COPY gradle ./gradle
RUN chmod +x ./gradlew
RUN ./gradlew dependencies --no-daemon

COPY src ./src
RUN ./gradlew bootJar --no-daemon

# Runtime 단계
FROM --platform=linux/amd64 eclipse-temurin:17-jdk

# 필요한 패키지만 설치
RUN apt-get update && apt-get install -y \
    curl \
    && rm -rf /var/lib/apt/lists/*

WORKDIR /app

# 애플리케이션 파일 복사
COPY --from=builder /app/build/libs/*.jar app.jar

# 환경 변수 설정
ENV SPRING_PROFILES_ACTIVE=prod
ENV PORT=8080
ENV SSL_ENABLED=false

# 고정 포트 노출
EXPOSE 8080

# 헬스체크 설정
HEALTHCHECK --interval=30s --timeout=3s \
  CMD curl -f http://localhost:8080/actuator/health || exit 1

# 애플리케이션 실행
ENTRYPOINT []
CMD ["java", "-Xms512m", "-Xmx1024m", "-XX:+UseG1GC", "-Dserver.port=8080", "-Dserver.ssl.enabled=false", "-Dspring.profiles.active=prod", "-jar", "/app/app.jar"]
