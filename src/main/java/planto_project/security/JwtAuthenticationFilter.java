package planto_project.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    private static final Set<String> PUBLIC_URLS = Set.of(
            "/auth/login",
            "/auth/refresh",
            "/auth/logout",
            "/test",
            "/health"
    );


    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getServletPath();
        return HttpMethod.OPTIONS.matches(request.getMethod())
                || PUBLIC_URLS.contains(path);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.debug("No JWT token found in request or not a Bearer token.");
            filterChain.doFilter(request, response);
            return;
        }

        final String token = authHeader.substring(7);

        String login;

        try {
            login = jwtUtil.extractSubjectFromToken(token);
        } catch (Exception e) {
            log.warn("Could not get username from JWT token, possibly invalid or malformed: {}", e.getMessage());
            filterChain.doFilter(request, response);
            return;
        }

        if (login != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            try {
                UserDetails userDetails = userDetailsService.loadUserByUsername(login);
                if (jwtUtil.validateJwtToken(token)) {
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    log.debug("User '{}' authenticated via JWT.", login);
                } else {
                    log.warn("JWT token is invalid for user '{}'", login);
                }
            } catch (Exception e) {
                log.error("Failed to authenticate user '{}': {}", login, e.getMessage(), e);
            }
        }

        filterChain.doFilter(request, response);
    }
}