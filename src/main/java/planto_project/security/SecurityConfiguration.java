package planto_project.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.WebExpressionAuthorizationManager;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final UserDetailsServiceImpl userDetailsService;
    private final JwtAuthEntryPoint jwtAuthEntryPoint;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(jwtAuthEntryPoint)
                        .accessDeniedHandler(customAccessDeniedHandler))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .authorizeHttpRequests(authorize -> authorize

                        // authentication endpoints
                        .requestMatchers("/auth/login", "/auth/refresh", "/auth/logout").permitAll()

                        // account endpoints
                        .requestMatchers("/account/register").permitAll()
                        .requestMatchers("/account/users")
                        .access(new WebExpressionAuthorizationManager("hasRole('ADMINISTRATOR')"))
                        .requestMatchers(HttpMethod.GET, "/account/user/{login}")
                        .access(new WebExpressionAuthorizationManager("hasRole('ADMINISTRATOR') or authentication.name == #login"))
                        .requestMatchers(HttpMethod.DELETE, "/account/user/{login}")
                        .access(new WebExpressionAuthorizationManager("hasRole('ADMINISTRATOR') or authentication.name == #login"))
                        .requestMatchers(HttpMethod.PUT, "/account/user/{login}")
                        .access(new WebExpressionAuthorizationManager("hasRole('ADMINISTRATOR') or authentication.name == #login"))
                        .requestMatchers(HttpMethod.PUT, "/account/user/{login}/role/{role}")
                        .access(new WebExpressionAuthorizationManager("hasRole('ADMINISTRATOR')"))
                        .requestMatchers(HttpMethod.DELETE, "/account/user/{login}/role/{role}")
                        .access(new WebExpressionAuthorizationManager("hasRole('ADMINISTRATOR')"))
                        .requestMatchers(HttpMethod.GET, "/account/user/{login}/cart")
                        .access(new WebExpressionAuthorizationManager("hasRole('ADMINISTRATOR') or authentication.name == #login"))
                        .requestMatchers(HttpMethod.PUT, "/account/user/{login}/cart/{productId}")
                        .access(new WebExpressionAuthorizationManager("hasRole('ADMINISTRATOR') or authentication.name == #login"))
                        .requestMatchers(HttpMethod.DELETE, "/account/user/{login}/cart/{productId}/**")
                        .access(new WebExpressionAuthorizationManager("hasRole('ADMINISTRATOR') or authentication.name == #login"))
                        .requestMatchers(HttpMethod.DELETE, "/account/user/{login}/cart/clear")
                        .access(new WebExpressionAuthorizationManager("hasRole('ADMINISTRATOR') or authentication.name == #login"))


                        // product endpoints
                        .requestMatchers(HttpMethod.GET, "/product", "/product/**", "/product/quantity").permitAll()
                        .requestMatchers(HttpMethod.POST, "/product").permitAll()
                        .requestMatchers("/product/create", "/product/update/**", "/product/filterdata")
//                        .requestMatchers("/product/create", "/product/update/**")
                        .access(new WebExpressionAuthorizationManager("hasRole('ADMINISTRATOR')"))
                        .requestMatchers(HttpMethod.DELETE, "/product/{productId}")
                        .access(new WebExpressionAuthorizationManager("hasRole('ADMINISTRATOR')"))
                        .requestMatchers(HttpMethod.PUT, "/product/**")
                        .access(new WebExpressionAuthorizationManager("hasRole('ADMINISTRATOR')"))

                        // order endpoints
                        .requestMatchers(HttpMethod.POST, "/order/create/{login}")
                        .access(new WebExpressionAuthorizationManager("authentication.name == #login"))
                        .requestMatchers(HttpMethod.GET, "/order/{login}/**")
                        .access(new WebExpressionAuthorizationManager("authentication.name == #login or hasRole('ADMINISTRATOR')"))
                        .requestMatchers(HttpMethod.PUT, "/order/{login}/**")
                        .access(new WebExpressionAuthorizationManager("authentication.name == #login or hasRole('ADMINISTRATOR')"))
                        .requestMatchers(HttpMethod.DELETE, "/order/remove/**")
                        .access(new WebExpressionAuthorizationManager("hasRole('ADMINISTRATOR')"))
                        .requestMatchers("/orders")
                        .access(new WebExpressionAuthorizationManager("hasRole('ADMINISTRATOR')"))

                        //images endpoint
                        .requestMatchers("/uploadImage")
                        .access(new WebExpressionAuthorizationManager("hasRole('ADMIN') or hasRole('ADMINISTRATOR')"))


                        // ping
                        .requestMatchers("/health").permitAll()

                        // for other requests
                        .anyRequest().authenticated());

        http.authenticationProvider(authenticationProvider());
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }


    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOriginPatterns(Arrays.asList(
                "http://localhost:*",
                "https://localhost:*",
                "https://planto-front.onrender.com"
        ));


        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

    @Bean
    public CorsFilter corsFilter() {
        return new CorsFilter(corsConfigurationSource());
    }
}