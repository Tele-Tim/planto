package planto_project.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.WebExpressionAuthorizationManager;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfiguration {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.cors(Customizer.withDefaults());
        http.httpBasic(Customizer.withDefaults());
        // todo out in production
        http.csrf(csrf -> csrf.disable());
        http.authorizeHttpRequests(authorize -> authorize
                // order
                .requestMatchers(HttpMethod.POST, "/order/{login}")
                .access(new WebExpressionAuthorizationManager("authentication.name == #login"))
                .requestMatchers(HttpMethod.GET, "/order/{login}/**")
                .access(new WebExpressionAuthorizationManager("authentication.name == #login or hasRole('ADMINISTRATOR')"))
                .requestMatchers(HttpMethod.PUT, "/order/{login}/**")
                .access(new WebExpressionAuthorizationManager("authentication.name == #login or hasRole('ADMINISTRATOR')"))
                .requestMatchers(HttpMethod.DELETE, "/order/{login}/**")
                .access(new WebExpressionAuthorizationManager("authentication.name == #login or hasRole('ADMINISTRATOR')"))

                // product
                .requestMatchers(HttpMethod.DELETE, "/product/**")
//                TODO change, when auth in front will be ready
//                .access(new WebExpressionAuthorizationManager("hasRole('ADMINISTRATOR')"))
                .permitAll()
                .requestMatchers(HttpMethod.PUT, "/product/update/**")
//                TODO change, when auth in front will be ready
//                .access(new WebExpressionAuthorizationManager("hasRole('ADMINISTRATOR')"))
                .permitAll()
                .requestMatchers(HttpMethod.GET, "/product/**").permitAll()
                .requestMatchers("/product/create")
//                TODO change, when auth in front will be ready
//                .access(new WebExpressionAuthorizationManager("hasRole('ADMINISTRATOR')"))
                .permitAll()
                // account
                .requestMatchers("/account/register").permitAll()
                //todo only for ADMINISTRATOR
                .requestMatchers(HttpMethod.GET,"/account/**").permitAll()
//
//                TODO change, when auth in front will be ready
//                .requestMatchers(HttpMethod.PUT, "/account/user/{login}")
//                .access(new WebExpressionAuthorizationManager("authentication.name == #login"))
//                .requestMatchers(HttpMethod.DELETE, "/account/user/{login}")
//                .access(new WebExpressionAuthorizationManager("hasRole('ADMINISTRATOR')" +
//                        "or authentication.name == #login"))
//                .requestMatchers(HttpMethod.PUT, "/account/user/{login}/role/{role}")
//                .access(new WebExpressionAuthorizationManager("hasRole('ADMINISTRATOR')"))
//                .requestMatchers(HttpMethod.DELETE, "/account/user/{login}/role/{role}")
//                .access(new WebExpressionAuthorizationManager("hasRole('ADMINISTRATOR')"))
                .anyRequest().authenticated());
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:5173"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Content-Type", "Authorization"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
