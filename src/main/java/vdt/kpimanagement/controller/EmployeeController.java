package vdt.kpimanagement.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import vdt.kpimanagement.dto.ApiResponse;
import vdt.kpimanagement.dto.EmployeeDTO;
import vdt.kpimanagement.repository.EmployeeRepo;
import vdt.kpimanagement.service.EmployeeService;

@RestController
@RequestMapping("/employees")
public class EmployeeController {
    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping("/")
    // todo -> admin
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse createEmployee(@RequestBody EmployeeDTO employeeDTO) {
        // todo -> validate dữ liệu đầu vào
        // todo -> gọi service để tạo nhân viên mới
        return ApiResponse.success(HttpStatus.CREATED.value(),
                "Employee created successfully",
                employeeService.createEmployee(employeeDTO));
    }

}
