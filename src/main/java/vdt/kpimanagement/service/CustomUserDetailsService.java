package vdt.kpimanagement.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import vdt.kpimanagement.entity.Account;
import vdt.kpimanagement.repository.AccountRepo;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final AccountRepo accountRepo;
    public CustomUserDetailsService(AccountRepo accountRepo) {
        this.accountRepo = accountRepo;
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Account> user = accountRepo.findByUsername(username);
        if (user.isPresent()) {
            Account account = user.get();
            return org.springframework.security.core.userdetails.User.withUsername(account.getUsername())
                    .password(account.getPassword())
//                    .roles(ac.getRole())
                    .build();
        } else
            throw new UsernameNotFoundException("User not found with username: " + username);

    }
}
