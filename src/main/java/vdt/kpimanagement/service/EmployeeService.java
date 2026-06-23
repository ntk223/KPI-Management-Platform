package vdt.kpimanagement.service;

import org.springframework.stereotype.Service;
import vdt.kpimanagement.dto.EmployeeRequest;
import vdt.kpimanagement.dto.EmployeeResponse;
import vdt.kpimanagement.entity.Department;
import vdt.kpimanagement.entity.Employee;
import vdt.kpimanagement.entity.Position;
import vdt.kpimanagement.exception.ResourceNotFoundException;
import vdt.kpimanagement.mapper.EmployeeMapper;
import vdt.kpimanagement.repository.DepartmentRepo;
import vdt.kpimanagement.repository.EmployeeRepo;
import vdt.kpimanagement.repository.PositionRepo;

@Service
public class EmployeeService extends BaseService<Employee, EmployeeRequest, EmployeeResponse, Long> {

    private final EmployeeRepo employeeRepo;
    private final DepartmentRepo departmentRepo;
    private final PositionRepo positionRepo;

    public EmployeeService(EmployeeRepo employeeRepo, EmployeeMapper employeeMapper,
                           DepartmentRepo departmentRepo, PositionRepo positionRepo) {
        super(employeeRepo, employeeMapper);
        this.employeeRepo = employeeRepo;
        this.departmentRepo = departmentRepo;
        this.positionRepo = positionRepo;
    }

    @Override
    public EmployeeResponse create(EmployeeRequest request) {
        Employee entity = mapper.toEntity(request);
        if (request.getEmployeeCode() == null || request.getEmployeeCode().trim().isEmpty()) {
            entity.setEmployeeCode(generateEmployeeCode());
        } else {
            if (employeeRepo.existsByEmployeeCodeAndIsDeletedFalse(request.getEmployeeCode())) {
                throw new IllegalArgumentException("Mã nhân viên đã tồn tại: " + request.getEmployeeCode());
            }
        }
        if (employeeRepo.existsByEmailAndIsDeletedFalse(request.getEmail())) {
            throw new IllegalArgumentException("Email đã tồn tại: " + request.getEmail());
        }
        entity.setDepartment(resolveDepartment(request.getDepartmentId()));
        entity.setPosition(resolvePosition(request.getPositionId()));
        entity.setDeleted(false);
        return mapper.toDto(employeeRepo.save(entity));
    }

    private String generateEmployeeCode() {
        long nextNum = employeeRepo.count() + 1;
        String code;
        do {
            code = String.format("EMP%03d", nextNum++);
        } while (employeeRepo.existsByEmployeeCodeAndIsDeletedFalse(code));
        return code;
    }

    @Override
    public EmployeeResponse update(Long id, EmployeeRequest request) {
        Employee entity = employeeRepo.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy nhân viên với ID: " + id));
        mapper.updateEntityFromDto(request, entity);
        entity.setDepartment(resolveDepartment(request.getDepartmentId()));
        entity.setPosition(resolvePosition(request.getPositionId()));
        return mapper.toDto(employeeRepo.save(entity));
    }

    private Department resolveDepartment(Long departmentId) {
        if (departmentId == null) return null;
        return departmentRepo.findByIdAndIsDeletedFalse(departmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy phòng ban với ID: " + departmentId));
    }

    private Position resolvePosition(Long positionId) {
        if (positionId == null) return null;
        return positionRepo.findByIdAndIsDeletedFalse(positionId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy chức vụ với ID: " + positionId));
    }
}
