package vdt.kpimanagement.controller;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import vdt.kpimanagement.dto.ApiResponse;
import vdt.kpimanagement.dto.LoginDTO;
import vdt.kpimanagement.dto.LoginInfoDTO;
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
    public ApiResponse<LoginInfoDTO> login(@RequestBody LoginDTO loginDTO) {
        return ApiResponse.success(HttpStatus.OK.value(), "",authService.login(loginDTO));
    }

    @GetMapping("/test")
    public Object test() {
        return ApiResponse.success(HttpStatus.OK.value(), "Test successful", null);
    }
}
