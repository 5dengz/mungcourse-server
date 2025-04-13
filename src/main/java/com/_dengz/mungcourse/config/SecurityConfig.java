package com._dengz.mungcourse.config;

import com._dengz.mungcourse.jwt.JwtFilter;
import com._dengz.mungcourse.jwt.TokenProvider;
import com._dengz.mungcourse.properties.SecurityProperties;
import com._dengz.mungcourse.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;


@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final TokenProvider tokenProvider;
    private final UserRepository userRepository;
    private final SecurityProperties securityProperties;
    private final AntPathMatcher pathMatcher = new AntPathMatcher();


    // 보안 필터 체인 설정 (CORS, CSRF, 전체 허용)
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(request -> {
                    CorsConfiguration config = new CorsConfiguration();
                    config.setAllowedOrigins(List.of("*")); // 실제 배포시에는 정확한 origin만 지정
                    config.setAllowedMethods(List.of("*"));
                    config.setAllowedHeaders(List.of("*"));
                    config.setAllowCredentials(true);
                    config.setExposedHeaders(List.of("Authorization"));
                    return config;
                }))
                .csrf(csrf -> csrf.disable())
                .formLogin(form -> form.disable())
                .httpBasic(basic -> basic.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtFilter(), UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
                        .requestMatchers(Stream.of(securityProperties.getPermitUrls())
                                .map(AntPathRequestMatcher::antMatcher) // 문자열 → AntPathRequestMatcher 객체로 변환
                                .toArray(AntPathRequestMatcher[]::new)) // AntPathRequestMatcher 객체를 배열로 변환
                        .permitAll()
                        .anyRequest().authenticated());

        return http.build();
    }

    @Bean
    public JwtFilter jwtFilter() {
        return new JwtFilter(tokenProvider, userRepository, securityProperties, pathMatcher);
    }


}