package vdt.kpimanagement.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import vdt.kpimanagement.entity.Account;

import java.security.Key;
import java.util.Date;
import java.util.UUID;

@Component
public class JwtTokenProvider {
    // Khóa bí mật dùng để ký token (Chuỗi phải đủ dài và an toàn)
    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private final long ACCESS_TOKEN_VALIDITY = 15 * 60 * 1000; // 15 phút

    // 1. Hàm tạo Access Token
    public String generateAccessToken(Account account) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + ACCESS_TOKEN_VALIDITY);

        return Jwts.builder()
                .setSubject(account.getUsername())
                .claim("employeeCode", account.getEmployee().getEmployeeCode())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(key)
                .compact();
    }

    // 2. Hàm tạo Refresh Token (Chuỗi ngẫu nhiên dài, không cần mã hóa phức tạp)
    public String generateRefreshToken() {
        return UUID.randomUUID().toString();
    }

    // 3. Hàm lấy username từ chuỗi JWT gửi lên
    public String getUsernameFromJWT(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    // 4. Hàm kiểm tra Token hợp lệ hay không
    public boolean validateToken(String authToken) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(authToken);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }
}