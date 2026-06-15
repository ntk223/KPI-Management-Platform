package vdt.kpimanagement.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import vdt.kpimanagement.dto.ApiResponse;
import vdt.kpimanagement.service.PositionService;

@RestController
@RequestMapping("/positions")
public class PositionController {

    private final PositionService positionService;

    public PositionController(PositionService positionService) {
        this.positionService = positionService;
    }

    @GetMapping
    public ApiResponse<Object> getAll() {
        return ApiResponse.success(HttpStatus.OK.value(), "Danh sách chức vụ", positionService.getAll());
    }

    @GetMapping("/{id}")
    public ApiResponse<Object> getById(@PathVariable Long id) {
        return ApiResponse.success(HttpStatus.OK.value(), "Chi tiết chức vụ", positionService.getById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<Object> create(@RequestBody Object request) {
        return ApiResponse.success(HttpStatus.CREATED.value(), "Tạo chức vụ thành công", positionService.create(request));
    }

    @PutMapping("/{id}")
    public ApiResponse<Object> update(@PathVariable Long id, @RequestBody Object request) {
        return ApiResponse.success(HttpStatus.OK.value(), "Cập nhật chức vụ thành công", positionService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Object> delete(@PathVariable Long id) {
        positionService.delete(id);
        return ApiResponse.success(HttpStatus.OK.value(), "Xoá chức vụ thành công");
    }
}
