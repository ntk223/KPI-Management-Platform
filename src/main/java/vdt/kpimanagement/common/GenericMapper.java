package vdt.kpimanagement.common;

import org.mapstruct.MappingTarget;

public interface GenericMapper<E, REQ, RESP> {
    RESP toDto(E entity);
    E toEntity(REQ dto);
    void updateEntityFromDto(REQ dto, @MappingTarget E entity);
}
