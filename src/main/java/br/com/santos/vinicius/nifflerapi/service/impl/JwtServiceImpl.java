package br.com.santos.vinicius.nifflerapi.service.impl;

import br.com.santos.vinicius.nifflerapi.model.UserAuthModel;
import br.com.santos.vinicius.nifflerapi.model.entity.UserAuthEntity;
import br.com.santos.vinicius.nifflerapi.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtServiceImpl implements JwtService {

    private static final long JWT_TOKEN_EXPIRATION_TIME = (60 * 60 * 8) * 1000;

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.issuer}")
    private String issuer;

    @Override
    public UserAuthModel generateToken(UserAuthEntity userAuthEntity) {
        Map<String, Object> claim = new HashMap<>();
        String newToken = doGenerateToken(claim, userAuthEntity.getUserName());
        Date expiresIn = getExpirationTimeFromToken(newToken);
        return new UserAuthModel(newToken, expiresIn.getTime());
    }

    @Override
    public boolean isTokenValid(String token) {
        return !isTokenExpired(token);
    }

    @Override
    public String getUserNameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    private Date getExpirationTimeFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    private <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);

        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(jwtSecret.getBytes()).parseClaimsJws(token).getBody();
    }

    private boolean isTokenExpired(String token) {
        final Date expiration = getExpirationTimeFromToken(token);
        return expiration.before(new Date());
    }


    private String doGenerateToken(Map<String, Object> claims, String subject) {
        return Jwts.builder().setClaims(claims).setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_EXPIRATION_TIME))
                .setIssuer(issuer)
                .signWith(SignatureAlgorithm.HS512, jwtSecret.getBytes()).compact();
    }
}
