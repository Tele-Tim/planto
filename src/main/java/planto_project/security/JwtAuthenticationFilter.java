package planto_project.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            logger.debug("No JWT token found in request or not a Bearer token.");
            filterChain.doFilter(request, response);
            return;
        }

        final String token = authHeader.substring(7);
        String username = null;

        try {
            username = jwtUtil.getUserNameFromJwtToken(token);
            logger.debug("Username extracted from JWT: {}", username);
        } catch (Exception e) {
            logger.warn("Could not get username from JWT token, possibly invalid or malformed: {}", e.getMessage());
            filterChain.doFilter(request, response);
            return;
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            logger.debug("Attempting to load UserDetails for username: {}", username);
            UserDetails userDetails = null;
            try {
                userDetails = userDetailsService.loadUserByUsername(username);
                logger.debug("UserDetails loaded for user: {}", username);
            } catch (UsernameNotFoundException e) {
                logger.warn("User '{}' not found in database: {}", username, e.getMessage());
                filterChain.doFilter(request, response);
                return;
            } catch (Exception e) {
                logger.error("An unexpected error occurred while loading UserDetails for '{}': {}", username, e.getMessage(), e);
                filterChain.doFilter(request, response);
                return;
            }

            if (jwtUtil.validateJwtToken(token)) {
                logger.debug("JWT token validated for user: {}", username);
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
                logger.info("User '{}' successfully authenticated and SecurityContext updated.", username);
            } else {
                logger.warn("JWT token is not valid for user '{}' after loading UserDetails.", username);
            }
        } else if (username != null && SecurityContextHolder.getContext().getAuthentication() != null) {
            logger.debug("User '{}' is already authenticated. Skipping JWT processing for this request.", username);
        } else {
            logger.debug("Username from JWT was null or authentication already exists, skipping further JWT processing.");
        }

        filterChain.doFilter(request, response);
    }
}