package com.alsvietnam.security.filter;

import com.alsvietnam.models.wrapper.ObjectResponseWrapper;
import com.alsvietnam.utils.Extensions;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Duc_Huy
 * Date: 9/4/2022
 * Time: 3:05 PM
 */

@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private JwtTokenProvider tokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        log.info("request path: {}", request.getServletPath());

        String authToken = getJwtFromRequest(request);
        if (!Extensions.isBlankOrNull(authToken)) {
            DecodedJWT decodedJWT;
            try {
                decodedJWT = tokenProvider.validateToken(authToken);
            } catch (JWTVerificationException e) {
                log.info("Error when verify token: {}", e.getMessage());
                ObjectResponseWrapper responseWrapper = ObjectResponseWrapper.builder()
                        .status(0)
                        .message(e.getMessage())
                        .build();
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                response.getOutputStream()
                        .println(objectMapper.writeValueAsString(responseWrapper));
                return;
            }

            String username = decodedJWT.getSubject();
            String userId = decodedJWT.getClaim("id").asString();
            String role = decodedJWT.getClaim("role").asString();
            Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority(role));
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(username, userId, authorities);
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            filterChain.doFilter(request, response);
        } else {
            filterChain.doFilter(request, response);
        }
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (!Extensions.isBlankOrNull(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
