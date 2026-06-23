package vdt.kpimanagement.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vdt.kpimanagement.entity.BaseEntity;
import vdt.kpimanagement.repository.BaseRepository;
import vdt.kpimanagement.common.GenericMapper;
import vdt.kpimanagement.exception.ResourceNotFoundException;

public abstract class BaseService<T extends BaseEntity, REQ, RESP, ID> {

    protected final BaseRepository<T, ID> repository;
    protected final GenericMapper<T, REQ, RESP> mapper;

    protected BaseService(BaseRepository<T, ID> repository, GenericMapper<T, REQ, RESP> mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public Page<RESP> getAll(Pageable pageable) {
        return repository.findByIsDeletedFalse(pageable).map(mapper::toDto);
    }

    /**
     * Tìm kiếm toàn văn theo keyword — mặc định gọi getAll (subclass ghi đè để tìm thực sự).
     */
    public Page<RESP> search(String keyword, Pageable pageable) {
        return getAll(pageable);
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
