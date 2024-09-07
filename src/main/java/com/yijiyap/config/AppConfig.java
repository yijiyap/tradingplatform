package com.yijiyap.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class AppConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http

//                Do not use server-side sessions to store user information. This works for JWT apps.
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))

//                Require all requests for `/api/**` to be authenticated
                .authorizeHttpRequests(Authorize -> Authorize
                        .requestMatchers("/api/**").authenticated()
                        .requestMatchers("/auth/**").permitAll()

//                        Others, allow them. Eg auth/signup
                        .anyRequest().permitAll())

//                Custom validator
                .addFilterBefore(new JwtTokenValidator(), UsernamePasswordAuthenticationFilter.class)

//                No need CSRF because we use JWT tokens in headers, not form parameters
                .csrf(csrf -> csrf.disable())

//                Configure CORS
                .cors(cors -> cors.configurationSource(corsConfigurationSource()));

//        Finalises SecurityFilterChain
        return http.build();

    }

    //   Restrict CORS to OUR backend, not randos
    private CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowCredentials(true);
        configuration.addAllowedOrigin("*");
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
