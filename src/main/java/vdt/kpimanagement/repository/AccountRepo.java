package vdt.kpimanagement.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vdt.kpimanagement.entity.Account;

import java.util.Optional;

public interface AccountRepo extends BaseRepository<Account, Long> {
    Optional<Account> findByUsername(String username);
    Optional<Account> findByEmployeeId(Long employeeId);

    @Query("""
            SELECT a FROM Account a LEFT JOIN a.employee e
            WHERE a.isDeleted = false
              AND (LOWER(a.username)     LIKE LOWER(CONCAT('%', :keyword, '%'))
                OR LOWER(e.fullName)     LIKE LOWER(CONCAT('%', :keyword, '%'))
                OR LOWER(e.email)        LIKE LOWER(CONCAT('%', :keyword, '%'))
                OR LOWER(e.employeeCode) LIKE LOWER(CONCAT('%', :keyword, '%')))
            """)
    Page<Account> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);
}
