package vdt.kpimanagement.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import vdt.kpimanagement.common.GenericMapper;
import vdt.kpimanagement.dto.KpiCycleRequest;
import vdt.kpimanagement.dto.KpiCycleResponse;
import vdt.kpimanagement.entity.KpiCycle;

@Mapper(componentModel = "spring")
public interface KpiCycleMapper extends GenericMapper<KpiCycle, KpiCycleRequest, KpiCycleResponse> {

    @Override
    KpiCycleResponse toDto(KpiCycle entity);

    @Override
    @Mapping(target = "createdBy", ignore = true) // set thủ công trong Service
    @Mapping(target = "status", ignore = true)     // mặc định PLANNING
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    KpiCycle toEntity(KpiCycleRequest dto);

    @Override
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromDto(KpiCycleRequest dto, @org.mapstruct.MappingTarget KpiCycle entity);
}
