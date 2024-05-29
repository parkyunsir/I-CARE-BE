package com.example.backend.security;
import com.example.backend.repository.ParentRepository;
import com.example.backend.model.ParentEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import java.util.Set;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.Date;

@Slf4j
@Service
public class TokenProvider {
    @Autowired
    private ParentRepository parentRepository;
    private static final String SECRET_KEY = "anNoLXNwcmluZ2Jvb3QtYW5kLWp3dC10dXRvcmlhbC10aGlzLWlzLWZvci1nZW5lcmF0aW5nLWp3dC1zZWNyZXRrZXktYmFzZTY0Cg==";

    public String create(ParentEntity parent) {
        Date expiryDate = Date.from(
                Instant.now()
                        .plus(1, ChronoUnit.DAYS)
        );
        return Jwts.builder()
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .setSubject(parent.getEmail())
                .setIssuer("social Login")
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .claim("id", parent.getParent_id())
                .compact();
    }

    public String createForOAuth(String kakao, String email, String nickname) {
        ParentEntity parent = parentRepository.findByEmail(email);
        Date expiryDate = Date.from(
                Instant.now()
                        .plus(1, ChronoUnit.DAYS)
        );
        return Jwts.builder()
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .setSubject(parent.getNickname())
                .setIssuer(kakao + " Login")
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .claim("id", parent.getParent_id())
                .compact();
    }

    public boolean validToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String validateAndGetParentId(String token) {
        Claims claims = getClaims(token);
        return claims.get("id", String.class);
    }

    public Authentication getAuthentication(String token) {
        Claims claims = getClaims(token);
        String userEmail = claims.getSubject(); // 토큰에서 이메일을 가져옴
        Set<SimpleGrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")); // 사용자의 권한 설정 (예시로 ROLE_USER)
        return new UsernamePasswordAuthenticationToken(
                userEmail, // 사용자 이름으로 이메일을 사용
                null, // 비밀번호 필드 (비밀번호 인증 방식이 아닌 경우 null 사용)
                authorities
        );
    }


    private Claims getClaims(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
    }
}
