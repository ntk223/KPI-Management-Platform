package vdt.kpimanagement.controller;

import org.springframework.web.bind.annotation.*;
import vdt.kpimanagement.dto.KpiCategoryRequest;
import vdt.kpimanagement.dto.KpiCategoryResponse;
import vdt.kpimanagement.entity.KpiCategory;
import vdt.kpimanagement.service.KpiCategoryService;

@RestController
@RequestMapping("/kpi-categories")
public class KpiCategoryController extends BaseController<KpiCategory, KpiCategoryRequest, KpiCategoryResponse, Long> {

    public KpiCategoryController(KpiCategoryService kpiCategoryService) {
        super(kpiCategoryService, "danh mục tiêu chí");
    }
}
