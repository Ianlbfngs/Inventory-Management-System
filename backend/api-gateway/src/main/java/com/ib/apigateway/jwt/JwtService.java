package com.ib.apigateway.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.Key;

@Service
public class JwtService {
    String secretKey = "3BzLKVccNFLySRXXBkKpkPdP4wSCSLuk4PBEHAfvjO/JVZuJW6THa4t/hhN1or4m7DOETdStb60tx3xJlrY0iw==";

    public Key getKey(){
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public Claims exctractClaims(String token){
     return Jwts.parser()
             .verifyWith((SecretKey) getKey())
             .build()
             .parseSignedClaims(token)
             .getPayload();
    }
}
