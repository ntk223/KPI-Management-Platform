package vdt.kpimanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import vdt.kpimanagement.entity.Role;
import vdt.kpimanagement.constant.enums.RoleCode;

import java.util.List;
import java.util.Optional;

public interface RoleRepo extends JpaRepository<Role, Long> {
    Optional<Role> findByCode(RoleCode code);

    @Query(
        value = """
                SELECT r.code
                FROM account_roles ar
                JOIN roles r ON ar.role_id = r.id
                JOIN accounts a ON ar.account_id = a.id
                WHERE a.username = :username
                """, nativeQuery = true
    )
    List<String> getRolesByUsername(String username);
}
