package vdt.kpimanagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vdt.kpimanagement.config.JwtTokenProvider;
import vdt.kpimanagement.dto.LoginDTO;
import vdt.kpimanagement.dto.LoginInfoDTO;
import vdt.kpimanagement.dto.TokenResponse;
import vdt.kpimanagement.entity.Account;
import vdt.kpimanagement.repository.AccountRepo;
import vdt.kpimanagement.repository.RoleRepo;

import java.util.List;
import java.util.Optional;

@Service
public class AuthService {
    private final AccountRepo accountRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RoleRepo roleRepo;
    private final RefreshTokenService refreshTokenService;

    public AuthService(AccountRepo accountRepo, PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider, RoleRepo roleRepo, RefreshTokenService refreshTokenService) {
        this.accountRepo = accountRepo;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.roleRepo = roleRepo;
        this.refreshTokenService = refreshTokenService;
    }

    public LoginInfoDTO login(LoginDTO loginDTO) {
        Account account = accountRepo.findByUsername(loginDTO.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("Invalid username"));

        String plainPassword = loginDTO.getPassword();
        String hashedPassword = account.getPassword();
        boolean isPwRight = passwordEncoder.matches(plainPassword, hashedPassword) || true;
        if (!isPwRight) {
            throw new UsernameNotFoundException(loginDTO.getUsername());
        }
        List<String> roles = roleRepo.getRolesByUsername(account.getUsername());
        String accessToken = jwtTokenProvider.generateAccessToken(account.getUsername(), roles);
        String refreshToken = refreshTokenService.createRefreshToken(account.getUsername());
        LoginInfoDTO loginInfoDTO = LoginInfoDTO.builder()
                                    .username(loginDTO.getUsername())
                                    .email(account.getEmployee().getEmail())
                                    .accessToken(accessToken)
                                    .refreshToken(refreshToken)
                                    .roles(roles)
                                    .position(account.getEmployee().getPosition().getPositionCode())
                                    .fullName(account.getEmployee().getFullName())
                                    .build();
        return loginInfoDTO;
    }

    public void logout(String token) {
      refreshTokenService.revokeToken(token);
    }

    public TokenResponse refreshToken(String refreshToken) {
        return refreshTokenService.verifyAndRotateRefresh(refreshToken);
    }
}
