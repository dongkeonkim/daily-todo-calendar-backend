# Build 단계
FROM eclipse-temurin:17-jdk AS builder

# 작업 디렉토리 설정
WORKDIR /app

# 소스 코드 복사
COPY . .

# Gradle 빌드
RUN ./gradlew bootJar --no-daemon

# Runtime 단계
FROM eclipse-temurin:17-jre

# 작업 디렉토리 설정
WORKDIR /app

# 빌드된 JAR 파일 복사
COPY --from=builder /app/build/libs/*.jar app.jar

# 환경변수로 설정 파일 경로를 지정
ENTRYPOINT ["java", "-Dspring.config.location=classpath:/application.yml,optional:/app/config/application.yml", "-jar", "app.jar"]
