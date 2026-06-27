package vdt.kpimanagement.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vdt.kpimanagement.dto.DepartmentMembersDTO;
import vdt.kpimanagement.dto.DepartmentRequest;
import vdt.kpimanagement.dto.DepartmentResponse;
import vdt.kpimanagement.dto.EmployeeResponse;
import vdt.kpimanagement.entity.Department;
import vdt.kpimanagement.entity.Employee;
import vdt.kpimanagement.exception.ResourceNotFoundException;
import vdt.kpimanagement.mapper.DepartmentMapper;
import vdt.kpimanagement.mapper.EmployeeMapper;
import vdt.kpimanagement.repository.DepartmentRepo;
import vdt.kpimanagement.repository.EmployeeRepo;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DepartmentService extends BaseService<Department, DepartmentRequest, DepartmentResponse, Long> {

    private final DepartmentRepo departmentRepo;
    private final EmployeeRepo employeeRepo;
    private final EmployeeMapper employeeMapper;

    public DepartmentService(DepartmentRepo departmentRepo, DepartmentMapper departmentMapper,
                             EmployeeRepo employeeRepo, EmployeeMapper employeeMapper) {
        super(departmentRepo, departmentMapper);
        this.departmentRepo = departmentRepo;
        this.employeeRepo = employeeRepo;
        this.employeeMapper = employeeMapper;
    }

    @Override
    public Page<DepartmentResponse> search(String keyword, Pageable pageable) {
        return departmentRepo.searchByKeyword(keyword, pageable).map(mapper::toDto);
    }

    @Override
    public DepartmentResponse create(DepartmentRequest request) {
        Department entity = mapper.toEntity(request);
        if (request.getDepartmentCode() == null || request.getDepartmentCode().trim().isEmpty()) {
            entity.setDepartmentCode(generateDepartmentCode());
        } else {
            if (departmentRepo.existsByDepartmentCodeAndIsDeletedFalse(request.getDepartmentCode())) {
                throw new IllegalArgumentException("Mã phòng ban đã tồn tại: " + request.getDepartmentCode());
            }
        }
        // Resolve parent nếu có
        if (request.getParentId() != null) {
            Department parent = departmentRepo.findByIdAndIsDeletedFalse(request.getParentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy phòng ban cha với ID: " + request.getParentId()));
            entity.setParent(parent);
        }

        entity.setDeleted(false);
        return mapper.toDto(departmentRepo.save(entity));
    }

    private String generateDepartmentCode() {
        long nextNum = departmentRepo.count() + 1;
        String code;
        do {
            code = String.format("DEPT%03d", nextNum++);
        } while (departmentRepo.existsByDepartmentCodeAndIsDeletedFalse(code));
        return code;
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

    /**
     * Lấy toàn bộ thành viên của phòng ban: nhân viên trực tiếp + danh sách phòng con kèm nhân viên.
     * Dùng cho role MANAGER xem chi tiết nhân sự phòng mình.
     */
    public DepartmentMembersDTO getDepartmentMembers(Long departmentId) {
        Department dept = departmentRepo.findByIdAndIsDeletedFalse(departmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy phòng ban với ID: " + departmentId));

        // 1. Nhân viên trực tiếp thuộc phòng này
        List<EmployeeResponse> directEmployees = employeeRepo
                .findByDepartment_IdAndIsDeletedFalse(departmentId)
                .stream()
                .map(employeeMapper::toDto)
                .collect(Collectors.toList());

        // 2. Các phòng con trực thuộc
        List<Department> subDepts = departmentRepo.findByParent_IdAndIsDeletedFalse(departmentId);
        List<DepartmentMembersDTO.SubDepartmentSummary> subDeptSummaries = subDepts.stream()
                .map(subDept -> {
                    List<Employee> subEmployees = employeeRepo
                            .findByDepartment_IdAndIsDeletedFalse(subDept.getId());

                    // Tên trưởng phòng con
                    String managerName = subEmployees.stream()
                            .filter(e -> e.getId().equals(subDept.getManagerId()))
                            .map(Employee::getFullName)
                            .findFirst()
                            .orElse(null);

                    return DepartmentMembersDTO.SubDepartmentSummary.builder()
                            .id(subDept.getId())
                            .departmentCode(subDept.getDepartmentCode())
                            .name(subDept.getName())
                            .managerId(subDept.getManagerId())
                            .managerName(managerName)
                            .employeeCount(subEmployees.size())
                            .employees(subEmployees.stream().map(employeeMapper::toDto).collect(Collectors.toList()))
                            .build();
                })
                .collect(Collectors.toList());

        return DepartmentMembersDTO.builder()
                .department((DepartmentResponse) mapper.toDto(dept))
                .employees(directEmployees)
                .subDepartments(subDeptSummaries)
                .build();
    }
}
