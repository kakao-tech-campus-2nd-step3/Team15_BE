package kakao.rebit.auth.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {

    private final SecretKey key;

    public JwtTokenProvider(@Value("${custom.jwt.secretKey}") String base64Secret) {
        this.key = Keys.hmacShaKeyFor(java.util.Base64.getDecoder().decode(base64Secret));
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

    // 토큰에서 역할 추출
    public String getRoleFromToken(String token) {
        return extractClaim(token).get("role", String.class);
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SignatureException e) {
            System.out.println("유효하지 않은 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            System.out.println("만료된 JWT 토큰입니다.");
        } catch (Exception e) {
            System.out.println("유효하지 않은 JWT 토큰입니다.");
        }
        return false;
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
