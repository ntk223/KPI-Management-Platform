package vdt.kpimanagement.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import vdt.kpimanagement.dto.ApiResponse;
import vdt.kpimanagement.service.KpiItemService;

@RestController
@RequestMapping("/kpi-items")
public class KpiItemController {

    private final KpiItemService kpiItemService;

    public KpiItemController(KpiItemService kpiItemService) {
        this.kpiItemService = kpiItemService;
    }

    // Lấy tất cả tiêu chí của 1 phiếu KPI
    @GetMapping
    public ApiResponse<Object> getByDocument(@RequestParam Long documentId) {
        return ApiResponse.success(HttpStatus.OK.value(), "Danh sách tiêu chí KPI",
                kpiItemService.getByDocument(documentId));
    }

    // Thêm tiêu chí vào phiếu KPI
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<Object> create(@RequestBody Object request) {
        return ApiResponse.success(HttpStatus.CREATED.value(), "Thêm tiêu chí thành công",
                kpiItemService.create(request));
    }

    // Cập nhật tiêu chí
    @PutMapping("/{id}")
    public ApiResponse<Object> update(@PathVariable Long id, @RequestBody Object request) {
        return ApiResponse.success(HttpStatus.OK.value(), "Cập nhật tiêu chí thành công",
                kpiItemService.update(id, request));
    }

    // Huỷ tiêu chí (status = CANCELLED)
    @PatchMapping("/{id}/cancel")
    public ApiResponse<Object> cancel(@PathVariable Long id) {
        kpiItemService.cancel(id);
        return ApiResponse.success(HttpStatus.OK.value(), "Huỷ tiêu chí thành công");
    }
}
