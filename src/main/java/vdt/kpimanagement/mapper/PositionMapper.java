package vdt.kpimanagement.mapper;

import org.mapstruct.Mapper;
import vdt.kpimanagement.common.GenericMapper;
import vdt.kpimanagement.dto.PositionRequest;
import vdt.kpimanagement.dto.PositionResponse;
import vdt.kpimanagement.entity.Position;

@Mapper(componentModel = "spring")
public interface PositionMapper extends GenericMapper<Position, PositionRequest, PositionResponse> {
}
