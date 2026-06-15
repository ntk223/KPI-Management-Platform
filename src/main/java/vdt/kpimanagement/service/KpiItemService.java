package vdt.kpimanagement.service;

import org.springframework.stereotype.Service;
import vdt.kpimanagement.repository.KpiItemRepo;
import vdt.kpimanagement.repository.KpiDocumentRepo;
import vdt.kpimanagement.repository.KpiTemplateRepo;

@Service
public class KpiItemService {

    private final KpiItemRepo kpiItemRepo;
    private final KpiDocumentRepo kpiDocumentRepo;
    private final KpiTemplateRepo kpiTemplateRepo;

    public KpiItemService(KpiItemRepo kpiItemRepo,
                          KpiDocumentRepo kpiDocumentRepo,
                          KpiTemplateRepo kpiTemplateRepo) {
        this.kpiItemRepo = kpiItemRepo;
        this.kpiDocumentRepo = kpiDocumentRepo;
        this.kpiTemplateRepo = kpiTemplateRepo;
    }

    // Lấy tất cả tiêu chí trong 1 phiếu KPI
    public Object getByDocument(Long documentId) {
        // TODO: trả về list KpiItemDTO theo document_id
        throw new UnsupportedOperationException("Chưa implement");
    }

    // Thêm tiêu chí vào phiếu KPI
    public Object create(Object request) {
        // TODO: validate document đang ở DRAFT hoặc APPROVED
        // TODO: validate tổng weight không vượt 100
        // TODO: nếu có template_id thì copy thông tin từ template
        throw new UnsupportedOperationException("Chưa implement");
    }

    // Cập nhật tiêu chí (chỉ khi phiếu còn DRAFT)
    public Object update(Long id, Object request) {
        // TODO: validate document.status = DRAFT
        // TODO: validate tổng weight không vượt 100 sau khi sửa
        throw new UnsupportedOperationException("Chưa implement");
    }

    // Huỷ tiêu chí (không xoá, chuyển status = CANCELLED)
    public void cancel(Long id) {
        // TODO: chỉ huỷ khi phiếu còn DRAFT hoặc APPROVED
        throw new UnsupportedOperationException("Chưa implement");
    }
}
