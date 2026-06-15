package vdt.kpimanagement.service;

import vdt.kpimanagement.entity.BaseEntity;
import vdt.kpimanagement.repository.BaseRepository;
import vdt.kpimanagement.common.GenericMapper;
import vdt.kpimanagement.exception.ResourceNotFoundException;

import java.util.List;

public abstract class BaseService<T extends BaseEntity, REQ, RESP, ID> {

    protected final BaseRepository<T, ID> repository;
    protected final GenericMapper<T, REQ, RESP> mapper;

    protected BaseService(BaseRepository<T, ID> repository, GenericMapper<T, REQ, RESP> mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public List<RESP> getAll() {
        return repository.findByIsDeletedFalse().stream()
                .map(mapper::toDto)
                .toList();
    }

    public RESP getById(ID id) {
        T entity = repository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy bản ghi với ID: " + id));
        return mapper.toDto(entity);
    }

    public RESP create(REQ request) {
        T entity = mapper.toEntity(request);
        entity.setDeleted(false);
        T savedEntity = repository.save(entity);
        return mapper.toDto(savedEntity);
    }

    public RESP update(ID id, REQ request) {
        T entity = repository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy bản ghi với ID: " + id));
        mapper.updateEntityFromDto(request, entity);
        T savedEntity = repository.save(entity);
        return mapper.toDto(savedEntity);
    }

    public void delete(ID id) {
        T entity = repository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy bản ghi với ID: " + id));
        entity.setDeleted(true);
        repository.save(entity);
    }
}
