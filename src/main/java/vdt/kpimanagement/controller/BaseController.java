package vdt.kpimanagement.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import vdt.kpimanagement.dto.ApiResponse;
import vdt.kpimanagement.entity.BaseEntity;
import vdt.kpimanagement.service.BaseService;

import java.util.List;

public abstract class BaseController<T extends BaseEntity, REQ, RESP, ID> {

    protected final BaseService<T, REQ, RESP, ID> service;
    protected final String resourceName;

    protected BaseController(BaseService<T, REQ, RESP, ID> service, String resourceName) {
        this.service = service;
        this.resourceName = resourceName;
    }

    @GetMapping
    public ApiResponse<List<RESP>> getAll() {
        return ApiResponse.success(HttpStatus.OK.value(), "Danh sách " + resourceName, service.getAll());
    }

    @GetMapping("/{id}")
    public ApiResponse<RESP> getById(@PathVariable ID id) {
        return ApiResponse.success(HttpStatus.OK.value(), "Chi tiết " + resourceName, service.getById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<RESP> create(@RequestBody REQ request) {
        return ApiResponse.success(HttpStatus.CREATED.value(), "Tạo " + resourceName + " thành công", service.create(request));
    }

    @PutMapping("/{id}")
    public ApiResponse<RESP> update(@PathVariable ID id, @RequestBody REQ request) {
        return ApiResponse.success(HttpStatus.OK.value(), "Cập nhật " + resourceName + " thành công", service.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable ID id) {
        service.delete(id);
        return ApiResponse.success(HttpStatus.OK.value(), "Xoá " + resourceName + " thành công");
    }
}
