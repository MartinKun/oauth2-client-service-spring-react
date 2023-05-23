package com.example.oauth2clientservice.security.config;

import com.example.oauth2clientservice.security.jwt.JwtAuthenticationFilter;
import com.example.oauth2clientservice.security.jwt.JwtEntryPoint;
import com.example.oauth2clientservice.security.jwt.JwtProvider;
import com.example.oauth2clientservice.security.oauth2.CustomOAuth2UserService;
import com.example.oauth2clientservice.security.oauth2.HttpCookieOAuth2AuthorizationRequestRepository;
import com.example.oauth2clientservice.security.oauth2.OAuth2AuthenticationFailureHandler;
import com.example.oauth2clientservice.security.oauth2.OAuth2AuthenticationSuccessHandler;
import com.example.oauth2clientservice.security.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtEntryPoint jwtEntryPoint;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final JwtProvider jwtProvider;
    private final Environment environment;
    private final RefreshTokenRepository refreshTokenRepository;

    private final LogoutHandler logoutHandler;

    @Bean
    public String getOauth2RedirectUri() {
        return environment.getProperty("app.oauth2.authorizedRedirectUri[0]");
    }

    /*
      By default, Spring OAuth2 uses HttpSessionOAuth2AuthorizationRequestRepository to save
      the authorization request. But, since our service is stateless, we can't save it in
      the session. We'll save the request in a Base64 encoded cookie instead.
    */
    @Bean
    public HttpCookieOAuth2AuthorizationRequestRepository cookieAuthorizationRequestRepository() {
        return new HttpCookieOAuth2AuthorizationRequestRepository();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {

        OAuth2AuthenticationSuccessHandler oauth2SuccessHandler = new OAuth2AuthenticationSuccessHandler(
                getOauth2RedirectUri(),
                jwtProvider,
                cookieAuthorizationRequestRepository(),
                refreshTokenRepository
        );
        OAuth2AuthenticationFailureHandler oAuth2FailureHandler = new OAuth2AuthenticationFailureHandler(
                cookieAuthorizationRequestRepository()
        );
        return http
                .cors()
                .and()
                .csrf()
                .disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeHttpRequests()
                .antMatchers("/api/v1/auth/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .exceptionHandling().authenticationEntryPoint(jwtEntryPoint)
                .and()
                .oauth2Login()
                .authorizationEndpoint()
                .baseUri("/oauth2/authorize")
                .authorizationRequestRepository(cookieAuthorizationRequestRepository())
                .and()
                .redirectionEndpoint()
                .baseUri("/oauth2/callback/*")
                .and()
                .userInfoEndpoint()
                .userService(customOAuth2UserService)
                .and()
                .successHandler(oauth2SuccessHandler)
                .failureHandler(oAuth2FailureHandler)
                .and()
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .logout()
                .logoutUrl("/api/v1/auth/logout")
                .addLogoutHandler(logoutHandler)
                .logoutSuccessHandler(
                        (request, response, authentication) -> SecurityContextHolder.clearContext()
                )
                .and()
                .build();
    }
}
