#!/bin/sh

while true; do
    sleep 90d

    sudo certbot renew --quiet --post-hook "\
        sudo chown -R spring:spring /etc/letsencrypt/live && \
        sudo chmod -R 750 /etc/letsencrypt/live && \
        openssl pkcs12 -export \
        -in /etc/letsencrypt/live/${DOMAIN_NAME}/fullchain.pem \
        -inkey /etc/letsencrypt/live/${DOMAIN_NAME}/privkey.pem \
        -out /etc/letsencrypt/live/${DOMAIN_NAME}/keystore.p12 \
        -name tomcat \
        -password pass:${SSL_KEY_STORE_PASSWORD}"
done