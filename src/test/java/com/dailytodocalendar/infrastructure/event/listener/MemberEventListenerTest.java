package com.dailytodocalendar.infrastructure.event.listener;

import static org.junit.jupiter.api.Assertions.*;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import org.junit.jupiter.api.Test;

class MemberEventListenerTest {

  @Test
  public void test() {
    SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS512);
    String base64Key = Encoders.BASE64.encode(key.getEncoded());
    System.out.println(base64Key);
  }
}
