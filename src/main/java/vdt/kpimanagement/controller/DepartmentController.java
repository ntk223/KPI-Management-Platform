package vdt.kpimanagement.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import vdt.kpimanagement.dto.ApiResponse;
import vdt.kpimanagement.service.DepartmentService;

@RestController
@RequestMapping("/departments")
public class DepartmentController {

    private final DepartmentService departmentService;

    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @GetMapping
    public ApiResponse<Object> getAll() {
        return ApiResponse.success(HttpStatus.OK.value(), "Danh sách phòng ban", departmentService.getAll());
    }

    @GetMapping("/{id}")
    public ApiResponse<Object> getById(@PathVariable Long id) {
        return ApiResponse.success(HttpStatus.OK.value(), "Chi tiết phòng ban", departmentService.getById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<Object> create(@RequestBody Object request) {
        return ApiResponse.success(HttpStatus.CREATED.value(), "Tạo phòng ban thành công", departmentService.create(request));
    }

    @PutMapping("/{id}")
    public ApiResponse<Object> update(@PathVariable Long id, @RequestBody Object request) {
        return ApiResponse.success(HttpStatus.OK.value(), "Cập nhật phòng ban thành công", departmentService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Object> delete(@PathVariable Long id) {
        departmentService.delete(id);
        return ApiResponse.success(HttpStatus.OK.value(), "Xoá phòng ban thành công");
    }
}
