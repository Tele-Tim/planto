package planto_project.security;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import planto_project.model.UserAccount;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtUtil {


    @Value("${planto.app.jwtSecret}")
    private String jwtSecret;

    @Getter
    @Value("${planto.app.jwtExpirationMs}")
    private long jwtExpirationMs;

    @Value("${planto.app.jwtRefreshSecret}")
    private String jwtRefreshSecret;

    @Getter
    @Value("${planto.app.jwtRefreshExpirationMs}")
    private long jwtRefreshExpirationMs;

    private SecretKey accessSigningKey;
    private SecretKey refreshSigningKey;

    @PostConstruct
    public void initSigningKeys() {
        try {
            this.accessSigningKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
            this.refreshSigningKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtRefreshSecret));
            log.info("JWT signing keys initialized");
        } catch (IllegalArgumentException e) {
            log.error("Failed to initialize signing keys: {}", e.getMessage());
            throw new IllegalStateException("Cannot initialize JWT signing keys", e);
        }
    }

    public String generateJwtToken(String subject) {
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + jwtExpirationMs);

        return Jwts.builder()
                .subject(subject)
                .issuedAt(now)
                .expiration(expirationDate)
                .signWith(accessSigningKey, Jwts.SIG.HS512)
                .compact();
    }

    public String extractSubjectFromToken(String token) {
        return Jwts.parser()
                .verifyWith(accessSigningKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public boolean validateJwtToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(accessSigningKey)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (JwtException e) {
            log.warn("Invalid JWT access token: {}", e.getMessage());
            return false;
        }
    }

  public String generateRefreshToken(String subject) {
    Date now = new Date();
    Date expirationDate = new Date(now.getTime() + jwtRefreshExpirationMs);

    return Jwts.builder()
            .subject(subject)
            .issuedAt(now)
            .expiration(expirationDate)
            .signWith(refreshSigningKey, Jwts.SIG.HS512)
            .compact();
  }

    public boolean validateRefreshToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(refreshSigningKey)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (JwtException e) {
            log.warn("Invalid refresh token: {}", e.getMessage());
            return false;
        }
    }

    public String extractRefreshSubject(String token) {
        return Jwts.parser()
                .verifyWith(refreshSigningKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }
}
