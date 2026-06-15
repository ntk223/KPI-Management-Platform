package vdt.kpimanagement.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import vdt.kpimanagement.common.GenericMapper;
import vdt.kpimanagement.dto.EmployeeRequest;
import vdt.kpimanagement.dto.EmployeeResponse;
import vdt.kpimanagement.entity.Employee;

@Mapper(componentModel = "spring")
public interface EmployeeMapper extends GenericMapper<Employee, EmployeeRequest, EmployeeResponse> {

    @Override
    @Mapping(source = "department.id", target = "departmentId")
    @Mapping(source = "department.name", target = "departmentName")
    @Mapping(source = "position.id", target = "positionId")
    @Mapping(source = "position.title", target = "positionTitle")
    EmployeeResponse toDto(Employee entity);

    @Override
    @Mapping(target = "department", ignore = true) // set thủ công trong Service
    @Mapping(target = "position", ignore = true)   // set thủ công trong Service
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Employee toEntity(EmployeeRequest dto);

    @Override
    @Mapping(target = "department", ignore = true)
    @Mapping(target = "position", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromDto(EmployeeRequest dto, @org.mapstruct.MappingTarget Employee entity);
}
