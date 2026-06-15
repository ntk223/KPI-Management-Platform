package vdt.kpimanagement.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import vdt.kpimanagement.common.GenericMapper;
import vdt.kpimanagement.dto.DepartmentRequest;
import vdt.kpimanagement.dto.DepartmentResponse;
import vdt.kpimanagement.entity.Department;

@Mapper(componentModel = "spring")
public interface DepartmentMapper extends GenericMapper<Department, DepartmentRequest, DepartmentResponse> {

    @Override
    @Mapping(source = "parent.id", target = "parentId")
    @Mapping(source = "parent.name", target = "parentName")
    DepartmentResponse toDto(Department entity);

    @Override
    @Mapping(target = "parent", ignore = true) // parent được set thủ công trong Service
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Department toEntity(DepartmentRequest dto);

    @Override
    @Mapping(target = "parent", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromDto(DepartmentRequest dto, @org.mapstruct.MappingTarget Department entity);
}
