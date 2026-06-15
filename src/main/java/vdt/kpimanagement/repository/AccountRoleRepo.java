package vdt.kpimanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vdt.kpimanagement.entity.AccountRole;

import java.util.List;

public interface AccountRoleRepo extends JpaRepository<AccountRole, AccountRole.AccountRoleId> {
    List<AccountRole> findByAccount_Id(Long accountId);
}
