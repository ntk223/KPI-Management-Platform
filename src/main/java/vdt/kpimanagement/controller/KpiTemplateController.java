package vdt.kpimanagement.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import vdt.kpimanagement.dto.ApiResponse;
import vdt.kpimanagement.dto.KpiTemplateRequest;
import vdt.kpimanagement.dto.KpiTemplateResponse;
import vdt.kpimanagement.entity.KpiTemplate;
import vdt.kpimanagement.service.KpiTemplateService;

import java.util.List;

@RestController
@RequestMapping("/kpi-templates")
public class KpiTemplateController extends BaseController<KpiTemplate, KpiTemplateRequest, KpiTemplateResponse, Long> {

    private final KpiTemplateService kpiTemplateService;

    public KpiTemplateController(KpiTemplateService kpiTemplateService) {
        super(kpiTemplateService, "tiêu chí mẫu");
        this.kpiTemplateService = kpiTemplateService;
    }

    // Lấy template theo danh mục (chỉ template đang active)
    @GetMapping("/by-category")
    public ApiResponse<List<KpiTemplateResponse>> getByCategory(@RequestParam Long categoryId) {
        return ApiResponse.success(HttpStatus.OK.value(),
                "Danh sách tiêu chí mẫu theo danh mục",
                kpiTemplateService.getByCategory(categoryId));
    }

    // Bật/tắt template
    @PatchMapping("/{id}/active")
    public ApiResponse<Void> toggleActive(@PathVariable Long id, @RequestParam boolean isActive) {
        kpiTemplateService.toggleActive(id, isActive);
        return ApiResponse.success(HttpStatus.OK.value(),
                isActive ? "Kích hoạt tiêu chí mẫu thành công" : "Vô hiệu hoá tiêu chí mẫu thành công");
    }
}
