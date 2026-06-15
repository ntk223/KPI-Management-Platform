package vdt.kpimanagement.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import vdt.kpimanagement.constant.enums.CycleStatus;
import vdt.kpimanagement.dto.ApiResponse;
import vdt.kpimanagement.dto.KpiCycleRequest;
import vdt.kpimanagement.dto.KpiCycleResponse;
import vdt.kpimanagement.entity.KpiCycle;
import vdt.kpimanagement.service.KpiCycleService;

@RestController
@RequestMapping("/kpi-cycles")
public class KpiCycleController extends BaseController<KpiCycle, KpiCycleRequest, KpiCycleResponse, Long> {

    private final KpiCycleService kpiCycleService;

    public KpiCycleController(KpiCycleService kpiCycleService) {
        super(kpiCycleService, "chu kỳ KPI");
        this.kpiCycleService = kpiCycleService;
    }

    // Chuyển trạng thái: PLANNING → ACTIVE → EVALUATING → CLOSED
    @PatchMapping("/{id}/status")
    public ApiResponse<KpiCycleResponse> changeStatus(@PathVariable Long id, @RequestParam CycleStatus status) {
        return ApiResponse.success(HttpStatus.OK.value(),
                "Cập nhật trạng thái chu kỳ thành công",
                kpiCycleService.changeStatus(id, status));
    }
}
