package vdt.kpimanagement.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import vdt.kpimanagement.dto.ApiResponse;
import vdt.kpimanagement.service.KpiDocumentEvaluationService;

@RestController
@RequestMapping("/kpi-evaluations")
public class KpiDocumentEvaluationController {

    private final KpiDocumentEvaluationService kpiEvaluationService;

    public KpiDocumentEvaluationController(KpiDocumentEvaluationService kpiEvaluationService) {
        this.kpiEvaluationService = kpiEvaluationService;
    }

    // Nhân viên tự đánh giá từng tiêu chí
    @PostMapping("/self/items/{kpiItemId}")
    public ApiResponse<Object> selfEvaluateItem(@PathVariable Long kpiItemId,
                                                 @RequestBody Object request) {
        return ApiResponse.success(HttpStatus.OK.value(), "Tự đánh giá tiêu chí thành công",
                kpiEvaluationService.selfEvaluateItem(kpiItemId, request));
    }

    // Nhân viên hoàn tất tự đánh giá toàn phiếu
    @PatchMapping("/self/documents/{documentId}/complete")
    public ApiResponse<Object> completeSelfEvaluation(@PathVariable Long documentId) {
        return ApiResponse.success(HttpStatus.OK.value(), "Hoàn tất tự đánh giá thành công",
                kpiEvaluationService.completeSelfEvaluation(documentId));
    }

    // Manager chấm điểm từng tiêu chí
    @PostMapping("/manager/items/{kpiItemId}")
    public ApiResponse<Object> managerEvaluateItem(@PathVariable Long kpiItemId,
                                                    @RequestBody Object request) {
        return ApiResponse.success(HttpStatus.OK.value(), "Chấm điểm tiêu chí thành công",
                kpiEvaluationService.managerEvaluateItem(kpiItemId, request));
    }

    // Manager chốt điểm tổng kết cả phiếu
    @PatchMapping("/manager/documents/{documentId}/complete")
    public ApiResponse<Object> completeManagerEvaluation(@PathVariable Long documentId) {
        return ApiResponse.success(HttpStatus.OK.value(), "Chốt điểm đánh giá thành công",
                kpiEvaluationService.completeManagerEvaluation(documentId));
    }

    // Xem kết quả đánh giá của 1 phiếu KPI
    @GetMapping("/documents/{documentId}")
    public ApiResponse<Object> getEvaluationResult(@PathVariable Long documentId) {
        return ApiResponse.success(HttpStatus.OK.value(), "Kết quả đánh giá KPI",
                kpiEvaluationService.getEvaluationResult(documentId));
    }
}
