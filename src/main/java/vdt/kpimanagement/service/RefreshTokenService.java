package vdt.kpimanagement.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import vdt.kpimanagement.config.JwtTokenProvider;
import vdt.kpimanagement.dto.TokenResponse;
import vdt.kpimanagement.entity.RedisRefreshToken;
import vdt.kpimanagement.repository.RoleRepo;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final RoleRepo roleRepo;
    private final JwtTokenProvider jwtProvider;

    private final Long refreshExpirationSeconds = 15 * 24 * 3600L;

    // Tiền tố (Prefix) giúp quản lý keys trong Redis một cách khoa học
    private static final String REDIS_KEY_PREFIX = "kpi:refresh_token:";

    public String createRefreshToken(String username) {
        // 1. Tạo chuỗi Opaque ngẫu nhiên hoàn toàn
        String tokenStr = UUID.randomUUID().toString();
        String redisKey = REDIS_KEY_PREFIX + tokenStr;

        // 2. Tạo object data để lưu trữ
        RedisRefreshToken redisToken = new RedisRefreshToken(tokenStr, username);

        // 3. Đẩy lên Redis và SET thời gian hết hạn (TTL) tự động
        redisTemplate.opsForValue().set(
                redisKey,
                redisToken,
                refreshExpirationSeconds,
                TimeUnit.SECONDS
        );

        return tokenStr;
    }

    public TokenResponse verifyAndRotateRefresh(String requestToken) {
        String redisKey = REDIS_KEY_PREFIX + requestToken;
        System.out.println(redisKey);
        // 1. Tìm kiếm token trên Redis
        RedisRefreshToken redisToken = (RedisRefreshToken) redisTemplate.opsForValue().get(redisKey);

        // Nếu không tìm thấy, nghĩa là token không tồn tại, hết hạn TTL và bị Redis tự động xóa
        if (redisToken == null) {
            throw new RuntimeException("Refresh token đã hết hạn hoặc không tồn tại trên hệ thống. Vui lòng đăng nhập lại.");
        }

        // 2. Xóa Token cũ ngay lập tức sau khi sử dụng (Cơ chế Rotation)
        redisTemplate.delete(redisKey);

        // 3. Lấy thông tin mới nhất từ DB để build AccessToken mới (Đảm bảo cập nhật Role/Phòng ban thời gian thực)
        List<String> roles = roleRepo.getRolesByUsername(redisToken.getUsername());


        String newAccessToken = jwtProvider.generateAccessToken(redisToken.getUsername(), roles);

        // 4. Sinh tiếp một cặp RefreshToken mới lưu lên Redis
        String newRefreshToken = createRefreshToken(redisToken.getUsername());

        return new TokenResponse(newAccessToken, newRefreshToken);
    }

    // Tính năng bổ sung: Cho phép hủy Token lập tức khi User chủ động ấn Đăng xuất (Logout)
    public void revokeToken(String requestToken) {
        String redisKey = REDIS_KEY_PREFIX + requestToken;
        redisTemplate.delete(redisKey);
    }
}
