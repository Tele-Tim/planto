package planto_project.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import planto_project.dao.AccountRepository;
import planto_project.dao.RefreshTokenRepository;
import planto_project.model.AuthRequest;
import planto_project.model.AuthResponse;
import planto_project.model.RefreshToken;
import planto_project.model.UserAccount;
import planto_project.security.JwtUtil;
import planto_project.security.MyHasher;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final AccountRepository accountRepository;
    private final JwtUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${app.jwt.refresh-token-expiration-days}")
    private int refreshTokenExpirationDays;

    @Value("${app.jwt.refresh-cookie-name}")
    private String refreshCookieName;

    public AuthResponse login(AuthRequest authRequest, HttpServletResponse httpServletResponse) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(authRequest.getLogin(), authRequest.getPassword());
        authenticationManager.authenticate(authenticationToken);

        UserAccount userAccount = accountRepository.findById(authRequest.getLogin())
                .orElseThrow(() -> new RuntimeException("Account not found"));

        String accessToken = jwtUtil.generateJwtToken(userAccount.getLogin(), userAccount);
        String refreshToken = jwtUtil.generateRefreshToken(userAccount.getLogin());
        String tokenHash = MyHasher.sha512Hex(refreshToken);

        refreshTokenRepository.save(RefreshToken.builder()
                .tokenHash(tokenHash)
                .expiryDate(Instant.now().plus(refreshTokenExpirationDays, ChronoUnit.DAYS))
                .revoked(false)
                .userLogin(userAccount.getLogin())
                .build());

        sendRefreshCookie(httpServletResponse, refreshToken);
        return new AuthResponse(accessToken, jwtUtil.getJwtExpirationMs());
    }

    public AuthResponse refresh(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        Optional<String> refreshToken = extractRefreshTokenFromCookie(httpServletRequest);
        if (refreshToken.isEmpty()) {
            throw new RuntimeException("Refresh token not found");
        }

        String refreshTokenHash = MyHasher.sha512Hex(refreshToken.get());
        RefreshToken tokenFromRepository = refreshTokenRepository.findByTokenHash(refreshTokenHash)
                .orElseThrow(() -> new RuntimeException("Refresh token not found"));

        if (tokenFromRepository.isRevoked() ||
                tokenFromRepository.getExpiryDate().isBefore(Instant.now())) {
            throw new RuntimeException("Refresh token expired or revoked");
        }

        UserAccount userAccount = accountRepository.findById(tokenFromRepository.getUserLogin())
                .orElseThrow(() -> new RuntimeException("User account not found"));

        String newAccessToken = jwtUtil.generateJwtToken(userAccount.getLogin(), userAccount);
        String newRefreshToken = jwtUtil.generateRefreshToken(userAccount.getLogin());

        tokenFromRepository.setRevoked(true);
        refreshTokenRepository.save(tokenFromRepository);

        refreshTokenRepository.save(RefreshToken.builder()
                .tokenHash(MyHasher.sha512Hex(newRefreshToken))
                .expiryDate(Instant.now().plus(refreshTokenExpirationDays, ChronoUnit.DAYS))
                .revoked(false)
                .userLogin(userAccount.getLogin())
                .build());

        sendRefreshCookie(httpServletResponse, newRefreshToken);

        return new AuthResponse(newAccessToken, jwtUtil.getJwtExpirationMs());
    }

    public void logout(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        Optional<String> refreshTokenFromCookie = extractRefreshTokenFromCookie(httpServletRequest);
        if (refreshTokenFromCookie.isEmpty()) {
            throw new RuntimeException("Refresh token not found");
        }
        String refreshTokenHash = MyHasher.sha512Hex(refreshTokenFromCookie.get());
        refreshTokenRepository.findByTokenHash(refreshTokenHash).ifPresent(refreshToken -> {
            refreshToken.setRevoked(true);
            refreshTokenRepository.save(refreshToken);
        });

        Cookie cookie = new Cookie(refreshCookieName, "");
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/auth/refresh");
        cookie.setMaxAge(0);
        httpServletResponse.addCookie(cookie);
    }


    private void sendRefreshCookie(HttpServletResponse httpServletResponse, String token) {
        Cookie refreshCookie = new Cookie(refreshCookieName, token);
        refreshCookie.setHttpOnly(true);
        refreshCookie.setSecure(true);
        refreshCookie.setPath("/auth/refresh");
        refreshCookie.setMaxAge((int)
                ChronoUnit.DAYS.getDuration().multipliedBy(refreshTokenExpirationDays).getSeconds()
        );
        //todo add refresh.setSameSite(true)
        httpServletResponse.addCookie(refreshCookie);
    }

    private Optional<String> extractRefreshTokenFromCookie(HttpServletRequest httpServletRequest) {
        if (httpServletRequest.getCookies() == null) {
            return Optional.empty();
        }
        for (Cookie cookie : httpServletRequest.getCookies()) {
            if (cookie.getName().equals(refreshCookieName)) {
                return Optional.of(cookie.getValue());
            }
        }
        return Optional.empty();
    }

}
