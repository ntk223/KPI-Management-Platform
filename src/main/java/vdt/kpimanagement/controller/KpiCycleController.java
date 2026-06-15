package vdt.kpimanagement.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import vdt.kpimanagement.constant.enums.CycleStatus;
import vdt.kpimanagement.dto.ApiResponse;
import vdt.kpimanagement.service.KpiCycleService;

@RestController
@RequestMapping("/kpi-cycles")
public class KpiCycleController {

    private final KpiCycleService kpiCycleService;

    public KpiCycleController(KpiCycleService kpiCycleService) {
        this.kpiCycleService = kpiCycleService;
    }

    @GetMapping
    public ApiResponse<Object> getAll() {
        return ApiResponse.success(HttpStatus.OK.value(), "Danh sách chu kỳ KPI", kpiCycleService.getAll());
    }

    @GetMapping("/{id}")
    public ApiResponse<Object> getById(@PathVariable Long id) {
        return ApiResponse.success(HttpStatus.OK.value(), "Chi tiết chu kỳ KPI", kpiCycleService.getById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<Object> create(@RequestBody Object request) {
        return ApiResponse.success(HttpStatus.CREATED.value(), "Tạo chu kỳ thành công", kpiCycleService.create(request));
    }

    @PutMapping("/{id}")
    public ApiResponse<Object> update(@PathVariable Long id, @RequestBody Object request) {
        return ApiResponse.success(HttpStatus.OK.value(), "Cập nhật chu kỳ thành công", kpiCycleService.update(id, request));
    }

    // Chuyển trạng thái chu kỳ (ADMIN only)
    // PLANNING → ACTIVE → EVALUATING → CLOSED
    @PatchMapping("/{id}/status")
    public ApiResponse<Object> changeStatus(@PathVariable Long id, @RequestParam CycleStatus status) {
        return ApiResponse.success(HttpStatus.OK.value(), "Cập nhật trạng thái chu kỳ thành công", kpiCycleService.changeStatus(id, status));
    }
}
