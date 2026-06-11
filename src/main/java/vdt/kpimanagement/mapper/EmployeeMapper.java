package vdt.kpimanagement.mapper;

import org.mapstruct.Mapper;
import vdt.kpimanagement.dto.EmployeeDTO;
import vdt.kpimanagement.entity.Employee;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {
    EmployeeDTO toDto(Employee employee);
    Employee toEntity(EmployeeDTO employeeDTO);
}
