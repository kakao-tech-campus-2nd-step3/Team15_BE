package kakao.rebit.auth.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import java.util.Date;
import javax.crypto.SecretKey;
import kakao.rebit.auth.jwt.exception.ExpiredTokenException;
import kakao.rebit.auth.jwt.exception.InvalidTokenException;
import kakao.rebit.auth.jwt.exception.MissingTokenException;
import kakao.rebit.auth.jwt.exception.SignatureValidationFailedException;
import kakao.rebit.auth.jwt.exception.UnsupportedTokenException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {
    private static final String BEARER_PREFIX = "Bearer ";
    private final SecretKey key;
    private final TokenBlacklistRepository tokenBlacklistRepository;

    public JwtTokenProvider(@Value("${custom.jwt.secretKey}") String base64Secret,
        TokenBlacklistRepository tokenBlacklistRepository) {
        this.key = Keys.hmacShaKeyFor(java.util.Base64.getDecoder().decode(base64Secret));
        this.tokenBlacklistRepository = tokenBlacklistRepository;
    }

    public String accessTokenGenerate(String uid, String email, String role, Date expiryDate) {
        return Jwts.builder()
            .setSubject(uid)
            .claim("email", email)
            .claim("role", role)
            .setExpiration(expiryDate)
            .signWith(key, SignatureAlgorithm.HS256)
            .compact();
    }

    public String refreshTokenGenerate(Date expiryDate) {
        return Jwts.builder()
            .setExpiration(expiryDate)
            .signWith(key, SignatureAlgorithm.HS256)
            .compact();
    }

    // 토큰에서 이메일 추출
    public String getEmailFromToken(String token) {
        return extractClaim(token).get("email", String.class);
    }

    public long getExpiration(String token) {
        return extractClaim(token).getExpiration().getTime();
    }

    public void addToBlacklist(String token) {
        long expirationTime = getExpiration(token) - System.currentTimeMillis();
        tokenBlacklistRepository.addToBlacklist(token, expirationTime);
    }

    public boolean validateToken(String token) {
        try {
            if (tokenBlacklistRepository.isBlacklisted(token)) {
                throw InvalidTokenException.EXCEPTION;
            }
            Jwts.parser().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SignatureException e) {
            throw SignatureValidationFailedException.EXCEPTION;
        } catch (ExpiredJwtException e) {
            throw ExpiredTokenException.EXCEPTION;
        } catch (Exception e) {
            throw InvalidTokenException.EXCEPTION;
        }
    }

    public String extractToken(String token){
        if (token == null) {
            throw MissingTokenException.EXCEPTION;
        }
        if (!token.startsWith(BEARER_PREFIX)) {
            throw UnsupportedTokenException.EXCEPTION;
        }
        return token.substring(BEARER_PREFIX.length());
    }

    // 토큰에서 클레임 정보 추출
    private Claims extractClaim(String token) {
        return Jwts.parser()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .getBody();
    }
}
