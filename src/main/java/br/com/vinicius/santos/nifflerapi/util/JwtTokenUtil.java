package br.com.vinicius.santos.nifflerapi.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

public class JwtTokenUtil {

    private static final String JWT_SECRET = System.getenv("JWT_SECRET");

    private static final String JWT_ISSUER = System.getenv("JWT_ISSUER");

    public boolean isTokenValid(String token) {

        Algorithm algorithm = Algorithm.HMAC256(JWT_SECRET);

        JWTVerifier jwtVerifier = JWT.require(algorithm)
                .withIssuer(JWT_ISSUER)
                .build();

        DecodedJWT jwt = jwtVerifier.verify(token);

        return jwt.getSignature() != null;
    }

}
