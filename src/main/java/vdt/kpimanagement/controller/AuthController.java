package vdt.kpimanagement.controller;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import vdt.kpimanagement.dto.*;
import vdt.kpimanagement.service.AuthService;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<LoginInfoDTO> login(@jakarta.validation.Valid @RequestBody LoginDTO loginDTO) {
        return ApiResponse.success(HttpStatus.OK.value(), "",authService.login(loginDTO));
    }

    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<String> logout(@jakarta.validation.Valid @RequestBody RefreshTokenRequest req) {
        authService.logout(req.getToken());
        return ApiResponse.success(HttpStatus.OK.value(),"Đăng xuất thành công");
    }

    @PostMapping("/refresh-token")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<TokenResponse> refreshToken(@jakarta.validation.Valid @RequestBody RefreshTokenRequest req) {
        return ApiResponse.success(HttpStatus.OK.value(), "", authService.refreshToken(req.getToken()));
    }

    @GetMapping("/test")
    public Object test() {
        return ApiResponse.success(HttpStatus.OK.value(), "Test successful", null);
    }
}
