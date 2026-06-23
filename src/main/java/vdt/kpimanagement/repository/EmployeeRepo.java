package vdt.kpimanagement.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vdt.kpimanagement.entity.Employee;

public interface EmployeeRepo extends BaseRepository<Employee, Long> {
    boolean existsByEmployeeCodeAndIsDeletedFalse(String employeeCode);
    boolean existsByEmailAndIsDeletedFalse(String email);

    @Query("""
            SELECT e FROM Employee e
            WHERE e.isDeleted = false
              AND (LOWER(e.employeeCode) LIKE LOWER(CONCAT('%', :keyword, '%'))
                OR LOWER(e.fullName)     LIKE LOWER(CONCAT('%', :keyword, '%'))
                OR LOWER(e.email)        LIKE LOWER(CONCAT('%', :keyword, '%'))
                OR LOWER(e.phoneNumber)  LIKE LOWER(CONCAT('%', :keyword, '%')))
            """)
    Page<Employee> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);
}
