package vdt.kpimanagement.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vdt.kpimanagement.dto.EmployeeDTO;
import vdt.kpimanagement.entity.Employee;
import vdt.kpimanagement.mapper.EmployeeMapper;
import vdt.kpimanagement.repository.EmployeeRepo;

@Service
public class EmployeeService {
    private final EmployeeRepo employeeRepo;
    private final EmployeeMapper employeeMapper;
    private final PasswordEncoder passwordEncoder;
    public EmployeeService(EmployeeRepo employeeRepo,EmployeeMapper employeeMapper, PasswordEncoder passwordEncoder) {
        this.employeeRepo = employeeRepo;
        this.employeeMapper = employeeMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean createEmployee(EmployeeDTO employeeDTO) {
        Employee employee = employeeMapper.toEntity(employeeDTO);
//        String hashPw = passwordEncoder.encode(employeeDTO.getPassword());
        employeeRepo.save(employee);
        return true;
    }

}
