package vdt.kpimanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vdt.kpimanagement.entity.Employee;

public interface EmployeeRepo extends JpaRepository<Employee, Long> {

}
