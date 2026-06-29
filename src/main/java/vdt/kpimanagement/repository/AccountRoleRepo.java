package vdt.kpimanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import vdt.kpimanagement.entity.AccountRole;

import java.util.List;

public interface AccountRoleRepo extends JpaRepository<AccountRole, AccountRole.AccountRoleId> {
    List<AccountRole> findByAccount_Id(Long accountId);
    boolean existsByRoleIdAndAccountId(Long roleId, Long accountId);
    void deleteByAccountIdAndRoleId(Long accountId, Long roleId);
    @Modifying // <--- RẤT QUAN TRỌNG
    @Query("DELETE FROM AccountRole ar WHERE ar.id.accountId = :accountId AND ar.id.roleId IN :roleIds")
    void deleteByAccountIdAndRoleIdIn(Long accountId, List<Long> roleIds);
}
