package vdt.kpimanagement.service;

import org.springframework.stereotype.Service;
import vdt.kpimanagement.entity.KpiDocumentEvaluation;
import vdt.kpimanagement.repository.*;

@Service
public class KpiDocumentEvaluationService {

    private final KpiItemEvaluationRepo kpiItemEvaluationRepo;
    private final KpiDocumentEvaluationRepo kpiEvaluationRepo;
    private final KpiDocumentRepo kpiDocumentRepo;
    private final KpiItemRepo kpiItemRepo;

    public KpiDocumentEvaluationService(KpiItemEvaluationRepo kpiItemEvaluationRepo,
                                KpiDocumentEvaluationRepo kpiEvaluationRepo,
                                 KpiDocumentRepo kpiDocumentRepo,
                                 KpiItemRepo kpiItemRepo) {
        this.kpiItemEvaluationRepo = kpiItemEvaluationRepo;
        this.kpiEvaluationRepo = kpiEvaluationRepo;
        this.kpiDocumentRepo = kpiDocumentRepo;
        this.kpiItemRepo = kpiItemRepo;
    }

    // Nhân viên tự đánh giá từng tiêu chí (self-evaluation)
    public Object selfEvaluateItem(Long kpiItemId, Object request) {
        // TODO: validate cycle.status = EVALUATING
        // TODO: validate document.status = IN_PROGRESS
        // TODO: lưu/cập nhật self_score, self_comment
        throw new UnsupportedOperationException("Chưa implement");
    }

    // Nhân viên hoàn tất tự đánh giá toàn bộ phiếu
    public Object completeSelfEvaluation(Long documentId) {
        // TODO: validate tất cả items đã có self_score
        // TODO: chuyển document.status = SELF_EVALUATED
        throw new UnsupportedOperationException("Chưa implement");
    }

    // Manager chấm điểm từng tiêu chí
    public Object managerEvaluateItem(Long kpiItemId, Object request) {
        // TODO: validate document.status = SELF_EVALUATED
        // TODO: lưu/cập nhật manager_score, manager_comment, final_score
        // TODO: cảnh báo nếu |self_score - manager_score| > 20
        throw new UnsupportedOperationException("Chưa implement");
    }

    // Manager chốt điểm tổng kết cả phiếu
    public Object completeManagerEvaluation(Long documentId) {
        // TODO: validate tất cả items đã có manager_score
        // TODO: tính total_final_score = Σ(final_score × weight / 100)
        // TODO: xác định rating theo thang điểm
        // TODO: lưu KpiEvaluation (kpi_document_evaluations)
        // TODO: chuyển document.status = MANAGER_EVALUATED
        throw new UnsupportedOperationException("Chưa implement");
    }

    // Lấy kết quả đánh giá của 1 phiếu KPI
    public Object getEvaluationResult(Long documentId) {
        // TODO: trả về KpiEvaluationDTO kèm chi tiết từng item
        throw new UnsupportedOperationException("Chưa implement");
    }
}
