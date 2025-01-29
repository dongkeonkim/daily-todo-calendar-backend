#!/bin/sh

# 도메인 이름이 설정되어 있는지 확인
if [ -z "$DOMAIN_NAME" ]; then
    echo "Error: DOMAIN_NAME environment variable is not set"
    exit 1
fi

# sshd 시작 시도를 로깅
echo "Starting sshd..."
sudo /usr/sbin/sshd
echo "sshd start attempted"

# DDNS 업데이트 스크립트를 백그라운드로 실행
echo "Starting DDNS update process..."
(while true; do /app/ddns-update.sh; sleep 1800; done) &
echo "DDNS update process started"

# Let's Encrypt 인증서 발급 (최초 실행시)
if [ ! -d "/etc/letsencrypt/live/${DOMAIN_NAME}" ]; then
    sudo certbot certonly --standalone \
        --non-interactive \
        --agree-tos \
        --email ${SSL_EMAIL} \
        -d ${DOMAIN_NAME} \
        --http-01-port=80

    # 인증서를 PKCS12 형식으로 변환
    openssl pkcs12 -export \
        -in /etc/letsencrypt/live/${DOMAIN_NAME}/fullchain.pem \
        -inkey /etc/letsencrypt/live/${DOMAIN_NAME}/privkey.pem \
        -out /etc/letsencrypt/live/${DOMAIN_NAME}/keystore.p12 \
        -name tomcat \
        -password pass:${SSL_KEY_STORE_PASSWORD}
fi

# 백그라운드에서 인증서 자동 갱신 스크립트 실행
/app/renew-cert.sh &

# Spring Boot 애플리케이션 실행
exec java -Xms512m -Xmx1024m -XX:+UseG1GC \
    -Dspring.profiles.active=${SPRING_PROFILES_ACTIVE} \
    -Dspring.config.location=classpath:/application.yml,optional:/app/config/application.yml \
    -jar app.jar