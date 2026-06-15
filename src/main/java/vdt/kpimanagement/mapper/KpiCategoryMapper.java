package vdt.kpimanagement.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import vdt.kpimanagement.common.GenericMapper;
import vdt.kpimanagement.dto.KpiCategoryRequest;
import vdt.kpimanagement.dto.KpiCategoryResponse;
import vdt.kpimanagement.entity.KpiCategory;

@Mapper(componentModel = "spring")
public interface KpiCategoryMapper extends GenericMapper<KpiCategory, KpiCategoryRequest, KpiCategoryResponse> {

    @Override
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    KpiCategory toEntity(KpiCategoryRequest dto);

    @Override
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromDto(KpiCategoryRequest dto, @org.mapstruct.MappingTarget KpiCategory entity);
}
