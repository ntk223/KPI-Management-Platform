package vdt.kpimanagement.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import vdt.kpimanagement.dto.ApiResponse;
import vdt.kpimanagement.entity.BaseEntity;
import vdt.kpimanagement.service.BaseService;

public abstract class BaseController<T extends BaseEntity, REQ, RESP, ID> {

    protected final BaseService<T, REQ, RESP, ID> service;
    protected final String resourceName;

    protected BaseController(BaseService<T, REQ, RESP, ID> service, String resourceName) {
        this.service = service;
        this.resourceName = resourceName;
    }

    /**
     * GET /resource?keyword=&page=&size=&sort=
     * Nếu keyword có giá trị → gọi service.search(); ngược lại → service.getAll()
     */
    @GetMapping
    public ApiResponse<Page<RESP>> getAll(
            @RequestParam(required = false) String keyword,
            @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {

        Page<RESP> result = StringUtils.hasText(keyword)
                ? service.search(keyword.trim(), pageable)
                : service.getAll(pageable);

        return ApiResponse.success(HttpStatus.OK.value(), "Danh sách " + resourceName, result);
    }

    @GetMapping("/{id}")
    public ApiResponse<RESP> getById(@PathVariable ID id) {
        return ApiResponse.success(HttpStatus.OK.value(), "Chi tiết " + resourceName, service.getById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<RESP> create(@jakarta.validation.Valid @RequestBody REQ request) {
        return ApiResponse.success(HttpStatus.CREATED.value(), "Tạo " + resourceName + " thành công", service.create(request));
    }

    @PutMapping("/{id}")
    public ApiResponse<RESP> update(@PathVariable ID id, @jakarta.validation.Valid @RequestBody REQ request) {
        return ApiResponse.success(HttpStatus.OK.value(), "Cập nhật " + resourceName + " thành công", service.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable ID id) {
        service.delete(id);
        return ApiResponse.success(HttpStatus.OK.value(), "Xoá " + resourceName + " thành công");
    }
}
