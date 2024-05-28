package com.example.backend.config;

import com.example.backend.config.oauth.OAuth2AuthenticationSuccessHandler;
import com.example.backend.config.oauth.UserOAuth2Service;
import com.example.backend.security.JwtAuthenticationFilter;
import com.example.backend.service.ParentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final UserOAuth2Service userOAuth2Service;
    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
    private final Environment env;

    @Bean
    public WebSecurityCustomizer configure() {
        return (web) -> web.ignoring()
                //.requestMatchers(toH2Console())
                .requestMatchers("/static/**");
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/parent/**").permitAll()
                        .anyRequest().authenticated())
                .logout(logout -> logout
                        .logoutSuccessUrl("/")
                        .invalidateHttpSession(true)) // 로그아웃 이후에 세션을 전체 삭제할지 여부
                .csrf(AbstractHttpConfigurer::disable)
                .oauth2Login(oauth2Login -> oauth2Login
                        .defaultSuccessUrl("/")
                        .successHandler(oAuth2AuthenticationSuccessHandler)
                        .userInfoEndpoint(userInfoEndpointConfig -> userInfoEndpointConfig
                                .userService(userOAuth2Service)));

        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http,
                                                       BCryptPasswordEncoder bCryptPasswordEncoder,
                                                       ParentService parentService) throws Exception {
        http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(parentService)
                .passwordEncoder(bCryptPasswordEncoder);
        return http.getSharedObject(AuthenticationManager.class);
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public ClientRegistrationRepository clientRegistrationRepository() {
        return new CustomClientRegistrationRepository(env);
    }

    private static class CustomClientRegistrationRepository implements ClientRegistrationRepository {
        private final Environment env;
        CustomClientRegistrationRepository(Environment env) {
            this.env = env;
        }

        @Override
        public ClientRegistration findByRegistrationId(String registrationId) {
            if("kakao".equals(registrationId)) {
                return ClientRegistration.withRegistrationId("kakao")
                        .clientId(env.getProperty("security.oauth2.client.registration.kakao.client-id"))
                        .clientSecret(env.getProperty("security.oauth2.client.registration.kakao.client-secret"))
                        .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                        .redirectUri("http://localhost:8080/login/oauth2/code/kakao")
                        .authorizationUri(env.getProperty("security.oauth2.client.provider.kakao.authorization-url"))
                        .tokenUri(env.getProperty("security.oauth2.client.provider.kakao.token-uri"))
                        .userInfoUri(env.getProperty("security.oauth2.client.provider.kakao.user-info-uri"))
                        .userNameAttributeName(env.getProperty("security.oauth2.client.provider.kakao.user-name-attribute"))
                        .clientName("Kakao")
                        .build();
            }
            return null;
        }
    }
}