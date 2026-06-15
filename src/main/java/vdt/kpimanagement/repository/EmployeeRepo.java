package vdt.kpimanagement.repository;

import vdt.kpimanagement.entity.Employee;

public interface EmployeeRepo extends BaseRepository<Employee, Long> {
    boolean existsByEmployeeCodeAndIsDeletedFalse(String employeeCode);
    boolean existsByEmailAndIsDeletedFalse(String email);
}
