package vdt.kpimanagement.service;

import org.springframework.stereotype.Service;
import vdt.kpimanagement.dto.EmployeeDTO;
import vdt.kpimanagement.entity.Employee;
import vdt.kpimanagement.mapper.EmployeeMapper;
import vdt.kpimanagement.repository.EmployeeRepo;

@Service
public class EmployeeService {
    private final EmployeeRepo employeeRepo;
    private final EmployeeMapper employeeMapper;

    public EmployeeService(EmployeeRepo employeeRepo
            ,EmployeeMapper employeeMapper
    ) {
        this.employeeRepo = employeeRepo;
        this.employeeMapper = employeeMapper;
    }

    public boolean createEmployee(EmployeeDTO employeeDTO) {
        Employee employee = employeeMapper.toEntity(employeeDTO);
        employeeRepo.save(employee);
        return true;
    }

}
