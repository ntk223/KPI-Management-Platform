package vdt.kpimanagement.service;

import org.springframework.stereotype.Service;
import vdt.kpimanagement.repository.KpiCategoryRepo;
import vdt.kpimanagement.repository.KpiTemplateRepo;

@Service
public class KpiTemplateService {

    private final KpiTemplateRepo kpiTemplateRepo;
    private final KpiCategoryRepo kpiCategoryRepo;

    public KpiTemplateService(KpiTemplateRepo kpiTemplateRepo, KpiCategoryRepo kpiCategoryRepo) {
        this.kpiTemplateRepo = kpiTemplateRepo;
        this.kpiCategoryRepo = kpiCategoryRepo;
    }

    // ─── Category ───────────────────────────────────────────

    // Lấy tất cả danh mục
    public Object getAllCategories() {
        // TODO: trả về list KpiCategoryDTO
        throw new UnsupportedOperationException("Chưa implement");
    }

    // Tạo danh mục mới
    public Object createCategory(Object request) {
        // TODO: validate, lưu DB
        throw new UnsupportedOperationException("Chưa implement");
    }

    // Cập nhật danh mục
    public Object updateCategory(Long id, Object request) {
        // TODO: validate, cập nhật
        throw new UnsupportedOperationException("Chưa implement");
    }

    // Xoá mềm danh mục
    public void deleteCategory(Long id) {
        // TODO: kiểm tra còn template đang dùng không
        throw new UnsupportedOperationException("Chưa implement");
    }

    // ─── Template ────────────────────────────────────────────

    // Lấy tất cả template (đang active)
    public Object getAllTemplates() {
        // TODO: trả về list KpiTemplateDTO
        throw new UnsupportedOperationException("Chưa implement");
    }

    // Lấy template theo danh mục
    public Object getTemplatesByCategory(Long categoryId) {
        // TODO: filter theo category_id
        throw new UnsupportedOperationException("Chưa implement");
    }

    // Tạo template mới
    public Object createTemplate(Object request) {
        // TODO: validate, gắn category nếu có, lưu DB
        throw new UnsupportedOperationException("Chưa implement");
    }

    // Cập nhật template
    public Object updateTemplate(Long id, Object request) {
        // TODO: validate, cập nhật
        throw new UnsupportedOperationException("Chưa implement");
    }

    // Bật/tắt template (is_active)
    public void toggleActive(Long id, boolean isActive) {
        // TODO: cập nhật is_active
        throw new UnsupportedOperationException("Chưa implement");
    }
}
