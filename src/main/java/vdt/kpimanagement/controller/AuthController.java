package vdt.kpimanagement.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vdt.kpimanagement.dto.ApiResponse;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @PostMapping("/login")
    public ApiResponse login(@RequestParam String username, @RequestParam String password) {
        return new ApiResponse(200, "Login successful", null);
    }
}
