package com.alsvietnam.security;

import com.alsvietnam.handler.CustomAccessDeniedHandler;
import com.alsvietnam.handler.CustomAuthenticationEntryPoint;
import com.alsvietnam.security.filter.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

/**
 * Duc_Huy
 * Date: 9/4/2022
 * Time: 11:35 AM
 */

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetailsService)
                .passwordEncoder(bCryptPasswordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors().configurationSource(corsConfigurationSource())
                .and()
                .csrf().disable();

        http
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http
                .authorizeRequests()
                .antMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html").permitAll()
                .antMatchers("/init/**", "/v1/auth/**").permitAll()
                .antMatchers(HttpMethod.GET, "/v1/roles/**").permitAll()
                .antMatchers(HttpMethod.GET, "/v1/topics/**").permitAll()
                .antMatchers(HttpMethod.GET, "/v1/articles/**").permitAll()
                .antMatchers(HttpMethod.GET,"/v1/donations/**").permitAll()
                .antMatchers(HttpMethod.POST, "/v1/donations").permitAll()
                .antMatchers(HttpMethod.GET, "/v1/users/**").permitAll()
                .antMatchers(HttpMethod.POST, "/v1/users/get-involve").permitAll()
                .antMatchers(HttpMethod.POST, "/v1/users/**/verify-account/**").permitAll()
                .antMatchers(HttpMethod.POST, "/v1/users/**/resend-email").permitAll()
                .antMatchers(HttpMethod.GET, "/v1/donation-campaigns/**").permitAll()
                .antMatchers(HttpMethod.GET, "/v1/comments/**").permitAll()
                .antMatchers(HttpMethod.GET, "/v1/stories/**").permitAll()
                .antMatchers(HttpMethod.GET, "/v1/honored-tables/**").permitAll()
                .antMatchers(HttpMethod.GET, "/v1/faq-categories/**").permitAll()
                .antMatchers(HttpMethod.GET, "/v1/faqs/**").permitAll()
                .antMatchers(HttpMethod.GET, "/v1/top-organization-supports/**").permitAll()
                .antMatchers("/ws", "/ws/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint())
                .accessDeniedHandler(accessDeniedHandler())
        ;

        // thêm 1 lớp Filter kiểm tra jwt
        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter();
    }

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return new CustomAuthenticationEntryPoint();
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new CustomAccessDeniedHandler();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(Collections.singletonList("*"));
        configuration.setAllowedMethods(Arrays.asList("HEAD", "GET", "PUT", "POST", "DELETE", "PATCH"));
        configuration.setAllowCredentials(true);
        //the below three lines will add the relevant CORS response headers
        configuration.addAllowedOriginPattern("*");
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
