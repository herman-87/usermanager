package cm.fastrelay.usermanager.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static cm.fastrelay.usermanager.config.security.Scope.USER_ADMIN;
import static cm.fastrelay.usermanager.config.security.Scope.USER_READ;

@Configuration
public class SecurityConfig {

    private final String REGEX_UUID_WITH_DELIMITER = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$";

    @Bean
    @Order(0)
    public SecurityFilterChain healthEndpoints(HttpSecurity http) throws Exception {
        return http.securityMatcher("/actuator/health/**", "/actuator/swagger-ui")
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
            .build();
    }

    @Bean
    @Order(1)
    public SecurityFilterChain publicEndpoints(HttpSecurity http) throws Exception {
        return http.securityMatcher("/public/**")
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(
                authorizationManagerRequestMatcherRegistry ->
                    authorizationManagerRequestMatcherRegistry
                        .requestMatchers("/public/**")
                        .permitAll())
            .build();
    }

    @Bean
    public SecurityFilterChain filterChain(
        HttpSecurity http,
        KeycloakJwtAuthenticationConverter keycloakJwtAuthenticationConverter) throws Exception {
        return http.csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(
                authorizationManagerRequestMatcherRegistry ->
                    authorizationManagerRequestMatcherRegistry
                        .requestMatchers(
                            HttpMethod.GET,
                            "/user/{id:" + REGEX_UUID_WITH_DELIMITER + "}"
                        ).hasRole(USER_READ)
                        .requestMatchers("/user/me")
                        .authenticated()
                        .requestMatchers(
                            HttpMethod.DELETE,
                            "/user/{id:" + REGEX_UUID_WITH_DELIMITER + "}"
                        ).hasRole(USER_ADMIN)
                        .requestMatchers(
                            HttpMethod.PUT,
                            "/user/me"
                        ).authenticated()
                        .anyRequest()
                        .denyAll())
            .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .oauth2ResourceServer(oauth2 ->
                oauth2.jwt(jwtConfigurer ->
                    jwtConfigurer.jwtAuthenticationConverter(keycloakJwtAuthenticationConverter)))
            .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
