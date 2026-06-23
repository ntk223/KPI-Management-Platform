package vdt.kpimanagement.service;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vdt.kpimanagement.entity.Account;
import vdt.kpimanagement.entity.AccountRole;
import vdt.kpimanagement.repository.AccountRepo;
import vdt.kpimanagement.repository.AccountRoleRepo;

import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final AccountRepo accountRepo;
    private final AccountRoleRepo accountRoleRepo;

    public CustomUserDetailsService(AccountRepo accountRepo, AccountRoleRepo accountRoleRepo) {
        this.accountRepo = accountRepo;
        this.accountRoleRepo = accountRoleRepo;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = accountRepo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy tài khoản: " + username));

        List<SimpleGrantedAuthority> authorities = accountRoleRepo.findByAccount_Id(account.getId())
                .stream()
                .map(AccountRole::getRole)
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getCode()))
                .toList();
        return org.springframework.security.core.userdetails.User
                .withUsername(account.getUsername())
                .password(account.getPassword())
                .authorities(authorities)
                .build();
    }
}
