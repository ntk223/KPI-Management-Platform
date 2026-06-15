package vdt.kpimanagement.controller;

import org.springframework.web.bind.annotation.*;
import vdt.kpimanagement.dto.DepartmentRequest;
import vdt.kpimanagement.dto.DepartmentResponse;
import vdt.kpimanagement.entity.Department;
import vdt.kpimanagement.service.DepartmentService;

@RestController
@RequestMapping("/departments")
public class DepartmentController extends BaseController<Department, DepartmentRequest, DepartmentResponse, Long> {

    public DepartmentController(DepartmentService departmentService) {
        super(departmentService, "phòng ban");
    }
}
