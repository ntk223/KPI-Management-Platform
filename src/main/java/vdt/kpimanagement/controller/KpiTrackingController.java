package vdt.kpimanagement.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import vdt.kpimanagement.dto.ApiResponse;
import vdt.kpimanagement.dto.KpiTrackingLogRequestDTO;
import vdt.kpimanagement.service.KpiTrackingService;

@RestController
@RequestMapping("/kpi-tracking")
public class KpiTrackingController {

    private final KpiTrackingService kpiTrackingService;

    public KpiTrackingController(KpiTrackingService kpiTrackingService) {
        this.kpiTrackingService = kpiTrackingService;
    }

    // Cập nhật tiến độ 1 tiêu chí KPI
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<Object> addProgress(@RequestBody KpiTrackingLogRequestDTO request) {
        return ApiResponse.success(HttpStatus.CREATED.value(), "Cập nhật tiến độ thành công",
                kpiTrackingService.addProgress(request));
    }

    // Lấy lịch sử cập nhật của 1 tiêu chí
    @GetMapping("/history")
    public ApiResponse<Object> getHistory(@RequestParam Long kpiItemId) {
        return ApiResponse.success(HttpStatus.OK.value(), "Lịch sử cập nhật tiến độ",
                kpiTrackingService.getHistory(kpiItemId));
    }

    // Lấy nhật ký cập nhật tiến độ gần đây
    @GetMapping("/recent")
    public ApiResponse<Object> getRecentLogs(
            @RequestParam(required = false) Long employeeId,
            @RequestParam(required = false) Long departmentId,
            @RequestParam(defaultValue = "10") int limit) {
        return ApiResponse.success(HttpStatus.OK.value(), "Nhật ký cập nhật tiến độ gần đây",
                kpiTrackingService.getRecentLogs(employeeId, departmentId, limit));
    }
}

