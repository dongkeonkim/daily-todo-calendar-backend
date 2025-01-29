#!/bin/sh

# sshd 시작 전에 호스트키 확인
if [ ! -f /etc/ssh/ssh_host_rsa_key ]; then
    ssh-keygen -A -f /etc
fi

# sshd 시작
echo "Starting sshd..."
sudo /usr/sbin/sshd
echo "sshd start attempted"

# DDNS 업데이트 실행
echo "Starting DDNS update process..."
/app/ddns-update.sh >> /var/log/ddns/update.log 2>&1 &
echo "DDNS update process started"

# DNS 전파 대기
echo "Waiting for DNS propagation..."
sleep 30

# Let's Encrypt 인증서 발급
if [ ! -d "/etc/letsencrypt/live/${DOMAIN_NAME}" ]; then
    sudo -E certbot certonly --standalone \
        --non-interactive \
        --agree-tos \
        --email ${SSL_EMAIL} \
        -d ${DOMAIN_NAME} \
        --http-01-port=80

    # 권한 설정 추가
    sudo chown -R spring:spring /etc/letsencrypt/live
    sudo chmod -R 750 /etc/letsencrypt/live

    # 인증서를 PKCS12 형식으로 변환
    sudo openssl pkcs12 -export \
        -in /etc/letsencrypt/live/${DOMAIN_NAME}/fullchain.pem \
        -inkey /etc/letsencrypt/live/${DOMAIN_NAME}/privkey.pem \
        -out /etc/letsencrypt/live/${DOMAIN_NAME}/keystore.p12 \
        -name tomcat \
        -password pass:${SSL_KEY_STORE_PASSWORD}

    # keystore.p12 파일 권한 설정
    sudo chown spring:spring /etc/letsencrypt/live/${DOMAIN_NAME}/keystore.p12
    sudo chmod 640 /etc/letsencrypt/live/${DOMAIN_NAME}/keystore.p12
fi

# Spring Boot 애플리케이션 실행
exec java -Xms512m -Xmx1024m -XX:+UseG1GC \
    -Dspring.profiles.active=${SPRING_PROFILES_ACTIVE} \
    -Dspring.config.location=classpath:/application.yml,optional:/app/config/application.yml \
    -jar app.jar