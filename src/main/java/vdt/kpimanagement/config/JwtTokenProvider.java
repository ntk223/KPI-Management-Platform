package vdt.kpimanagement.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import vdt.kpimanagement.dto.LoginInfoDTO;
import vdt.kpimanagement.entity.Account;

import java.security.Key;
import java.util.*;

@Component
public class JwtTokenProvider {
    // Khóa bí mật dùng để ký token (Chuỗi phải đủ dài và an toàn)
    @Value("${jwt.accesstoken.secret}")
    private final String ACCESS_TOKEN_SECRET;

    @Value("${jwt.refreshtoken.secret}")
    private final String REFRESH_TOKEN_SECRET;
    private final long ACCESS_TOKEN_VALIDITY = 15 * 60 * 1000; // 15 phút

    public JwtTokenProvider(@Value("${jwt.accesstoken.secret}") String accessTokenSecret,
                            @Value("${jwt.refreshtoken.secret}") String refreshTokenSecret) {
        this.REFRESH_TOKEN_SECRET = refreshTokenSecret;
        this.ACCESS_TOKEN_SECRET = accessTokenSecret;
    }

    private Key getJwtKey() {
        byte[] encodedKey = Base64.getEncoder().encode(this.ACCESS_TOKEN_SECRET.getBytes());
        return Keys.hmacShaKeyFor(encodedKey);
    }
    // 1. Hàm tạo Access Token
    public String generateAccessToken(String username, List<String> roles) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + ACCESS_TOKEN_VALIDITY);
        Map<String, Object>  claims = new HashMap<>();
        claims.put("roles",  roles);
        return Jwts.builder()
                .setSubject(username)
                .addClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getJwtKey())
                .compact();
    }

    // 3. Hàm lấy username từ chuỗi JWT gửi lên
    public String getUsernameFromJWT(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getJwtKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    // 4. Hàm kiểm tra Token hợp lệ hay không
    public boolean validateToken(String authToken) {
        try {
            Jwts.parserBuilder().setSigningKey(getJwtKey()).build().parseClaimsJws(authToken);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }
}