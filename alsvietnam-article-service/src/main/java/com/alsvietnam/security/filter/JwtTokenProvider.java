package com.alsvietnam.security.filter;

import com.alsvietnam.entities.User;
import com.alsvietnam.utils.DateUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;


/**
 * Duc_Huy
 * Date: 9/4/2022
 * Time: 2:31 PM
 */

@Service
@Slf4j
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String JWT_SECRET;

    @Value("${jwt.expired}")
    private int JWT_ACCESS_TOKEN_EXPIRATION;

    @Value("${jwt.issuer}")
    private String ISSUER;

    public String generateAccessToken(User user) {
        Algorithm algorithm = Algorithm.HMAC256(JWT_SECRET.getBytes());
        return JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(DateUtil.getDateAfterNumberTimes(new Date(), JWT_ACCESS_TOKEN_EXPIRATION, DateUtil.DAILY))
                .withClaim("id", user.getId())
                .withClaim("username", user.getUsername())
                .withClaim("role", user.getRole().getName())
                .withIssuer(ISSUER)
                .sign(algorithm);
    }

    public DecodedJWT validateToken(String authToken) {
        Algorithm algorithm = Algorithm.HMAC256(JWT_SECRET.getBytes());
        JWTVerifier verifier = JWT.require(algorithm)
                .withIssuer(ISSUER)
                .build();
        DecodedJWT decodedJWT;
        try {
            decodedJWT = verifier.verify(authToken);
        } catch (JWTVerificationException e) {
            throw new JWTVerificationException("Invalid access token: " + e.getMessage());
        }
        return decodedJWT;
    }
}
