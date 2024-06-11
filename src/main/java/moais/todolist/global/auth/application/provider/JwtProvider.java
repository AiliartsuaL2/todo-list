package moais.todolist.global.auth.application.provider;

import io.jsonwebtoken.*;
import moais.todolist.global.exception.ErrorMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.Date;

@Component
public class JwtProvider implements CreateTokenProvider, GetTokenInfoProvider {
    private final String secretKey;
    private final long accessTokenValidTime;
    private final long refreshTokenValidTime;

    public JwtProvider(
            @Value("${jwt.secret-key}") String secretKey,
            @Value("${jwt.expired-time.access-token-min}") long accessTokenValidMinute,
            @Value("${jwt.expired-time.refresh-token-day}") long refreshTokenValidDay) {
        this.secretKey = secretKey;
        this.accessTokenValidTime = 1000 * 60 * accessTokenValidMinute;
        this.refreshTokenValidTime = 1000 * 60 * 60 * 24 * refreshTokenValidDay;
    }

    @Override
    public String createAccessToken(String payload) {
        return createToken(payload, accessTokenValidTime);
    }

    @Override
    public String createRefreshToken(String payload) {
        return createToken(payload, refreshTokenValidTime);
    }

    private String createToken(final String payload, final Long validityInMilliseconds) {
        if (ObjectUtils.isEmpty(payload)) {
            throw new IllegalArgumentException(ErrorMessage.NOT_EXIST_PAYLOAD.getMessage());
        }

        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);
        return Jwts.builder()
                .setSubject(payload)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    @Override
    public String getPayload(String token) {
        if (ObjectUtils.isEmpty(token)) {
            throw new IllegalArgumentException(ErrorMessage.NOT_EXIST_TOKEN.getMessage());
        }

        if (!isValid(token)) {
            throw new IllegalArgumentException(ErrorMessage.INVALID_TOKEN.getMessage());
        }

        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    @Override
    public boolean isValid(String token) {
        try {
            Jws<Claims> claims = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token);
            return claims.getBody()
                    .getExpiration()
                    .after(new Date());
        } catch (SignatureException e) {
            throw new JwtException(ErrorMessage.SIGNATURE_TOKEN_EXCEPTION.getMessage());
        } catch (MalformedJwtException e) {
            throw new JwtException(ErrorMessage.MALFORMED_TOKEN.getMessage());
        } catch (ExpiredJwtException e) {
            throw new JwtException(ErrorMessage.EXPIRED_TOKEN.getMessage());
        }
    }
}
