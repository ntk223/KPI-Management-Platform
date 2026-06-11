package vdt.kpimanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vdt.kpimanagement.entity.Account;

import java.util.Optional;

public interface AccountRepo extends JpaRepository<Account, Long> {
     Optional<Account> findByUsername(String username);
}
