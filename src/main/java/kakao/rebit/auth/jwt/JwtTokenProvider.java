package kakao.rebit.auth.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
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

    public String accessTokenGenerate(String uid, Date expiryDate) {
        return Jwts.builder()
            .setSubject(uid)
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
}
