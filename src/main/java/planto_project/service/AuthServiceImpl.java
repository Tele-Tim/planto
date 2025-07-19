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
import planto_project.dto.AuthRequestDto;
import planto_project.dto.AuthResponseDto;
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

    @Override
    public AuthResponseDto login(AuthRequestDto authRequestDto, HttpServletResponse httpServletResponse) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(authRequestDto.getLogin(), authRequestDto.getPassword());
        authenticationManager.authenticate(authenticationToken);

        UserAccount userAccount = accountRepository.findById(authRequestDto.getLogin())
                .orElseThrow(() -> new RuntimeException("Account not found"));

        String accessToken = jwtUtil.generateJwtToken(userAccount.getLogin());
        String refreshToken = jwtUtil.generateRefreshToken(userAccount.getLogin());
        String tokenHash = MyHasher.sha512Hex(refreshToken);

        refreshTokenRepository.save(RefreshToken.builder()
                .tokenHash(tokenHash)
                .expiryDate(Instant.now().plus(refreshTokenExpirationDays, ChronoUnit.DAYS))
                .revoked(false)
                .userLogin(userAccount.getLogin())
                .build());

        sendRefreshCookie(httpServletResponse, refreshToken);
        return new AuthResponseDto(accessToken, jwtUtil.getJwtExpirationMs());
    }

    @Override
    public AuthResponseDto refresh(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        Optional<String> refreshToken = extractRefreshTokenFromCookie(httpServletRequest);
        if (refreshToken.isEmpty()) {
            throw new RuntimeException("Refresh token not found");
        }
        if (!jwtUtil.validateRefreshToken(refreshToken.get())) {
            throw new RuntimeException("Invalid refresh token");
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

        String newAccessToken = jwtUtil.generateJwtToken(userAccount.getLogin());
        String newRefreshToken = jwtUtil.generateRefreshToken(userAccount.getLogin());

        tokenFromRepository.setRevoked(true);
        refreshTokenRepository.save(tokenFromRepository);

        refreshTokenRepository.save(RefreshToken.builder()
                .tokenHash(MyHasher.sha512Hex(newRefreshToken))
                .expiryDate(Instant.now().plus(refreshTokenExpirationDays, ChronoUnit.DAYS))
                .revoked(false)
                .userLogin(userAccount.getLogin())
                .build());

        clearRefreshCookie(httpServletResponse);
        sendRefreshCookie(httpServletResponse, newRefreshToken);

        return new AuthResponseDto(newAccessToken, jwtUtil.getJwtExpirationMs());
    }

    @Override
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

        clearRefreshCookie(httpServletResponse);

    }

    private void clearRefreshCookie(HttpServletResponse response) {
        Cookie cookie = new Cookie(refreshCookieName, "");
        cookie.setHttpOnly(true);
//        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }


    private void sendRefreshCookie(HttpServletResponse httpServletResponse, String token) {
        int maxAge = (int) ChronoUnit.DAYS.getDuration()
                .multipliedBy(refreshTokenExpirationDays).getSeconds();

//        Cookie cookie = new Cookie(refreshCookieName, token);
//        cookie.setHttpOnly(true);
//        cookie.setSecure(true);
//        cookie.setPath("/");
//        cookie.setPath("/auth/refresh");
//        cookie.setMaxAge(maxAge);
//        cookie.setSameSite("Strict");
//        httpServletResponse.addCookie(cookie);

        String cookieValue = refreshCookieName + "=" + token +
                "; Path=/" +
                "; HttpOnly" +
//                "; Secure" +
//                "; SameSite=None" +
                "; SameSite=Lax" +
                "; Max-Age=" + maxAge;

        httpServletResponse.addHeader("Set-Cookie", cookieValue);
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
