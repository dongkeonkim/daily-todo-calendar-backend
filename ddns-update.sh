#!/bin/sh

# 환경변수 체크
if [ -z "$DDNS_PASSWORD" ]; then
    echo "Error: DDNS_PASSWORD is not set"
    exit 1
fi

# DDNS 업데이트 실행
curl -s "https://dynamicdns.park-your-domain.com/update?host=v3.api&domain=dailytodocalendar.com&password=${DDNS_PASSWORD}"