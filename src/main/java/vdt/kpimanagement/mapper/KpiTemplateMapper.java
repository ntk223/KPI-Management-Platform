package vdt.kpimanagement.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import vdt.kpimanagement.common.GenericMapper;
import vdt.kpimanagement.dto.KpiTemplateRequest;
import vdt.kpimanagement.dto.KpiTemplateResponse;
import vdt.kpimanagement.entity.KpiTemplate;

@Mapper(componentModel = "spring")
public interface KpiTemplateMapper extends GenericMapper<KpiTemplate, KpiTemplateRequest, KpiTemplateResponse> {

    @Override
    @Mapping(source = "category.id", target = "categoryId")
    @Mapping(source = "category.name", target = "categoryName")
    KpiTemplateResponse toDto(KpiTemplate entity);

    @Override
    @Mapping(target = "category", ignore = true) // set thủ công trong Service
    @Mapping(target = "active", ignore = true)    // set thủ công trong Service (mặc định true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    KpiTemplate toEntity(KpiTemplateRequest dto);

    @Override
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromDto(KpiTemplateRequest dto, @org.mapstruct.MappingTarget KpiTemplate entity);
}
