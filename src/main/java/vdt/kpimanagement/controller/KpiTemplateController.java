package vdt.kpimanagement.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import vdt.kpimanagement.dto.ApiResponse;
import vdt.kpimanagement.service.KpiTemplateService;

@RestController
@RequestMapping("/kpi-templates")
public class KpiTemplateController {

    private final KpiTemplateService kpiTemplateService;

    public KpiTemplateController(KpiTemplateService kpiTemplateService) {
        this.kpiTemplateService = kpiTemplateService;
    }

    // ─── Category ────────────────────────────────────────────────────

    @GetMapping("/categories")
    public ApiResponse<Object> getAllCategories() {
        return ApiResponse.success(HttpStatus.OK.value(), "Danh sách danh mục tiêu chí", kpiTemplateService.getAllCategories());
    }

    @PostMapping("/categories")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<Object> createCategory(@RequestBody Object request) {
        return ApiResponse.success(HttpStatus.CREATED.value(), "Tạo danh mục thành công", kpiTemplateService.createCategory(request));
    }

    @PutMapping("/categories/{id}")
    public ApiResponse<Object> updateCategory(@PathVariable Long id, @RequestBody Object request) {
        return ApiResponse.success(HttpStatus.OK.value(), "Cập nhật danh mục thành công", kpiTemplateService.updateCategory(id, request));
    }

    @DeleteMapping("/categories/{id}")
    public ApiResponse<Object> deleteCategory(@PathVariable Long id) {
        kpiTemplateService.deleteCategory(id);
        return ApiResponse.success(HttpStatus.OK.value(), "Xoá danh mục thành công");
    }

    // ─── Template ────────────────────────────────────────────────────

    @GetMapping
    public ApiResponse<Object> getAllTemplates(@RequestParam(required = false) Long categoryId) {
        if (categoryId != null) {
            return ApiResponse.success(HttpStatus.OK.value(), "Danh sách tiêu chí mẫu", kpiTemplateService.getTemplatesByCategory(categoryId));
        }
        return ApiResponse.success(HttpStatus.OK.value(), "Danh sách tiêu chí mẫu", kpiTemplateService.getAllTemplates());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<Object> createTemplate(@RequestBody Object request) {
        return ApiResponse.success(HttpStatus.CREATED.value(), "Tạo tiêu chí mẫu thành công", kpiTemplateService.createTemplate(request));
    }

    @PutMapping("/{id}")
    public ApiResponse<Object> updateTemplate(@PathVariable Long id, @RequestBody Object request) {
        return ApiResponse.success(HttpStatus.OK.value(), "Cập nhật tiêu chí mẫu thành công", kpiTemplateService.updateTemplate(id, request));
    }

    @PatchMapping("/{id}/active")
    public ApiResponse<Object> toggleActive(@PathVariable Long id, @RequestParam boolean isActive) {
        kpiTemplateService.toggleActive(id, isActive);
        return ApiResponse.success(HttpStatus.OK.value(), isActive ? "Kích hoạt tiêu chí mẫu thành công" : "Vô hiệu hoá tiêu chí mẫu thành công");
    }
}
