package planto_project.security;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtUtil {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);

    @Value("${planto.app.jwtSecret}")
    private String jwtSecret;

    @Value("${planto.app.jwtExpirationMs}")
    private int jwtExpirationMs;

    private SecretKey cachedSigningKey;

    @PostConstruct
    public void initSigningKey() {
        logger.info("Initializing signing key");
        try {
            byte[] keyBytes = Base64.getDecoder().decode(jwtSecret);
            cachedSigningKey = Keys.hmacShaKeyFor(keyBytes);
        } catch (IllegalArgumentException e) {
            logger.error(e.getMessage());
            throw new IllegalStateException("Cannot initialize JWT signing key", e);
        }
    }

    private SecretKey getSigningKey() {
        if (cachedSigningKey == null) {
            throw new IllegalStateException("Signing key not initialized");
        }
        return cachedSigningKey;
    }

    public String generateJwtToken(String username) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + jwtExpirationMs);
        return Jwts.builder()
                .subject(username)
                .issuedAt(now)
                .expiration(expiration)
                .signWith(getSigningKey(), Jwts.SIG.HS512)
                .compact();
    }

    public String getUserNameFromJwtToken(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(authToken);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }
}
