package vdt.kpimanagement.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import vdt.kpimanagement.dto.ApiResponse;
import vdt.kpimanagement.dto.DepartmentMembersDTO;
import vdt.kpimanagement.dto.DepartmentRequest;
import vdt.kpimanagement.dto.DepartmentResponse;
import vdt.kpimanagement.entity.Department;
import vdt.kpimanagement.service.DepartmentService;

@RestController
@RequestMapping("/departments")
public class DepartmentController extends BaseController<Department, DepartmentRequest, DepartmentResponse, Long> {

    private final DepartmentService departmentService;

    public DepartmentController(DepartmentService departmentService) {
        super(departmentService, "phòng ban");
        this.departmentService = departmentService;
    }

    /**
     * GET /departments/{id}/members
     * Trả về toàn bộ nhân viên + phòng ban con của phòng ban theo ID.
     * Dùng cho MANAGER xem chi tiết nhân sự phòng mình quản lý.
     */
    @GetMapping("/{id}/members")
    public ApiResponse<DepartmentMembersDTO> getDepartmentMembers(@PathVariable Long id) {
        return ApiResponse.success(
                HttpStatus.OK.value(),
                "Danh sách thành viên phòng ban",
                departmentService.getDepartmentMembers(id)
        );
    }
}
