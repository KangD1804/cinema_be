package com.codegym.c11.security;

import com.codegym.c11.model.entity.Account;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.security.PublicKey;
import java.util.Date;

@Component
@Service
public class JwtProvider {

    private static final String SECRET_KEY = "cgcinema11";
    private static final long EXPIRE_TIME = 1000 * 60 * 1000;
    private static final Logger logger = LoggerFactory.getLogger(JwtProvider.class.getName());

    public String generateTokenLogin(Account account) {

        return Jwts.builder()
                .setSubject(account.getUsername())
                .claim("username", account.getUsername())
                .setSubject((account.getUsername()))
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + EXPIRE_TIME * 1000))
                .signWith(SignatureAlgorithm.HS256,SECRET_KEY )
                .compact();
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(authToken);
            return true;
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token -> Message: {}", e);
        } catch (ExpiredJwtException e) {
            logger.error("Expired JWT token -> Message: {}", e);
        } catch (UnsupportedJwtException e) {
            logger.error("Unsupported JWT token -> Message: {}", e);
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty -> Message: {}", e);
        }

        return false;
    }

    public String getUserNameFromJwtToken(String token) {
        String userName = Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody().getSubject();
        return userName;
    }

    private byte[] generateShareSecret(){
        byte[] key = new byte[1000];
        key = SECRET_KEY.getBytes();
        return key;
    }

    private Key getKey(){
        return new PublicKey() {
            @Override
            public String getAlgorithm() {
                return null;
            }

            @Override
            public String getFormat() {
                return null;
            }

            @Override
            public byte[] getEncoded() {
                return generateShareSecret();
            }
        };
    }
}
