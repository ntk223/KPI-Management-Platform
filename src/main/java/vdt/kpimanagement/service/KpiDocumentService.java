package vdt.kpimanagement.service;

import org.springframework.stereotype.Service;
import vdt.kpimanagement.constant.enums.DocumentStatus;
import vdt.kpimanagement.constant.enums.DocumentTargetType;
import vdt.kpimanagement.repository.KpiCycleRepo;
import vdt.kpimanagement.repository.KpiDocumentRepo;
import vdt.kpimanagement.repository.EmployeeRepo;
import vdt.kpimanagement.repository.DepartmentRepo;

@Service
public class KpiDocumentService {

    private final KpiDocumentRepo kpiDocumentRepo;
    private final KpiCycleRepo kpiCycleRepo;
    private final EmployeeRepo employeeRepo;
    private final DepartmentRepo departmentRepo;

    public KpiDocumentService(KpiDocumentRepo kpiDocumentRepo,
                              KpiCycleRepo kpiCycleRepo,
                              EmployeeRepo employeeRepo,
                              DepartmentRepo departmentRepo) {
        this.kpiDocumentRepo = kpiDocumentRepo;
        this.kpiCycleRepo = kpiCycleRepo;
        this.employeeRepo = employeeRepo;
        this.departmentRepo = departmentRepo;
    }

    // Lấy KPI theo target (COMPANY / DEPARTMENT / EMPLOYEE) trong 1 chu kỳ
    public Object getByTarget(Long cycleId, DocumentTargetType targetType, Long targetId) {
        // TODO: query theo cycle + target_type + target_id
        throw new UnsupportedOperationException("Chưa implement");
    }

    // Lấy KPI cá nhân của nhân viên đang đăng nhập
    public Object getMyDocument(Long cycleId, Long employeeId) {
        // TODO: tìm document của employee trong cycle
        throw new UnsupportedOperationException("Chưa implement");
    }

    // Tạo phiếu KPI mới (DIRECTOR/MANAGER tạo, hoặc EMPLOYEE đề xuất)
    public Object create(Object request) {
        // TODO: validate cycle đang ACTIVE
        // TODO: validate target_type + target_id hợp lệ (polymorphic check)
        // TODO: kiểm tra unique (cycle_id, target_type, target_id)
        // TODO: set status = DRAFT, source_type theo người tạo
        throw new UnsupportedOperationException("Chưa implement");
    }

    // Gửi phiếu chờ duyệt (EMPLOYEE đề xuất)
    public Object submit(Long documentId) {
        // TODO: DRAFT → PENDING_APPROVAL
        // TODO: set submitted_at = now()
        throw new UnsupportedOperationException("Chưa implement");
    }

    // Duyệt phiếu KPI đề xuất (MANAGER)
    public Object approve(Long documentId, Long approverId) {
        // TODO: PENDING_APPROVAL → APPROVED → IN_PROGRESS
        // TODO: set approved_at = now(), approver_id
        throw new UnsupportedOperationException("Chưa implement");
    }

    // Từ chối phiếu KPI đề xuất (MANAGER)
    public Object reject(Long documentId, String reason) {
        // TODO: PENDING_APPROVAL → REJECTED
        throw new UnsupportedOperationException("Chưa implement");
    }

    // Đóng phiếu KPI cuối kỳ
    public Object close(Long documentId) {
        // TODO: MANAGER_EVALUATED → CLOSED
        // TODO: set closed_at = now()
        throw new UnsupportedOperationException("Chưa implement");
    }

    // Xem danh sách phiếu KPI chờ duyệt (MANAGER)
    public Object getPendingApprovals(Long managerId) {
        // TODO: lấy các document source_type=PROPOSED, status=PENDING_APPROVAL thuộc phòng manager quản lý
        throw new UnsupportedOperationException("Chưa implement");
    }
}
