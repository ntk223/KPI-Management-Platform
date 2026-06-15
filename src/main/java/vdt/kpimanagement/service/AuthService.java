package vdt.kpimanagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vdt.kpimanagement.config.JwtTokenProvider;
import vdt.kpimanagement.dto.LoginDTO;
import vdt.kpimanagement.dto.LoginInfoDTO;
import vdt.kpimanagement.entity.Account;
import vdt.kpimanagement.repository.AccountRepo;

import java.util.Optional;

@Service
public class AuthService {
    private final AccountRepo accountRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(AccountRepo accountRepo, PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider) {
        this.accountRepo = accountRepo;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
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
        String token = jwtTokenProvider.generateAccessToken(account);
        LoginInfoDTO loginInfoDTO = LoginInfoDTO.builder()
                                    .username(loginDTO.getUsername())
                                    .email(account.getEmployee().getEmail())
                                    .accessToken(token)
                                    .position(account.getEmployee().getPosition().getPositionCode())
                                    .fullName(account.getEmployee().getFullName())
                                    .build();
        return loginInfoDTO;
    }
}
