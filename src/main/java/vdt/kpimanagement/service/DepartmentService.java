package vdt.kpimanagement.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vdt.kpimanagement.dto.DepartmentRequest;
import vdt.kpimanagement.dto.DepartmentResponse;
import vdt.kpimanagement.entity.Department;
import vdt.kpimanagement.exception.ResourceNotFoundException;
import vdt.kpimanagement.mapper.DepartmentMapper;
import vdt.kpimanagement.repository.DepartmentRepo;

@Service
public class DepartmentService extends BaseService<Department, DepartmentRequest, DepartmentResponse, Long> {

    private final DepartmentRepo departmentRepo;

    public DepartmentService(DepartmentRepo departmentRepo, DepartmentMapper departmentMapper) {
        super(departmentRepo, departmentMapper);
        this.departmentRepo = departmentRepo;
    }

    @Override
    public Page<DepartmentResponse> search(String keyword, Pageable pageable) {
        return departmentRepo.searchByKeyword(keyword, pageable).map(mapper::toDto);
    }

    @Override
    public DepartmentResponse create(DepartmentRequest request) {
        if (departmentRepo.existsByDepartmentCodeAndIsDeletedFalse(request.getDepartmentCode())) {
            throw new IllegalArgumentException("Mã phòng ban đã tồn tại: " + request.getDepartmentCode());
        }
        Department entity = mapper.toEntity(request);
        // Resolve parent nếu có
        if (request.getParentId() != null) {
            Department parent = departmentRepo.findByIdAndIsDeletedFalse(request.getParentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy phòng ban cha với ID: " + request.getParentId()));
            entity.setParent(parent);
        }
        entity.setDeleted(false);
        return mapper.toDto(departmentRepo.save(entity));
    }

    @Override
    public DepartmentResponse update(Long id, DepartmentRequest request) {
        Department entity = departmentRepo.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy phòng ban với ID: " + id));
        mapper.updateEntityFromDto(request, entity);
        // Cập nhật parent nếu có thay đổi
        if (request.getParentId() != null) {
            Department parent = departmentRepo.findByIdAndIsDeletedFalse(request.getParentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy phòng ban cha với ID: " + request.getParentId()));
            entity.setParent(parent);
        } else {
            entity.setParent(null);
        }
        return mapper.toDto(departmentRepo.save(entity));
    }
}
