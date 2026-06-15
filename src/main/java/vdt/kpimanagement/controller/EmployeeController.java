package vdt.kpimanagement.controller;

import org.springframework.web.bind.annotation.*;
import vdt.kpimanagement.dto.EmployeeRequest;
import vdt.kpimanagement.dto.EmployeeResponse;
import vdt.kpimanagement.entity.Employee;
import vdt.kpimanagement.service.EmployeeService;

@RestController
@RequestMapping("/employees")
public class EmployeeController extends BaseController<Employee, EmployeeRequest, EmployeeResponse, Long> {

    public EmployeeController(EmployeeService employeeService) {
        super(employeeService, "nhân viên");
    }
}
