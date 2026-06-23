package vdt.kpimanagement.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Service;
import vdt.kpimanagement.constant.enums.DocumentStatus;
import vdt.kpimanagement.constant.enums.DocumentTargetType;
import vdt.kpimanagement.dto.KpiDocumentDetailDTO;
import vdt.kpimanagement.dto.KpiDocumentSaveDTO;
import vdt.kpimanagement.entity.Employee;
import vdt.kpimanagement.entity.KpiCycle;
import vdt.kpimanagement.entity.KpiDocument;
import vdt.kpimanagement.exception.BadRequestException;
import vdt.kpimanagement.exception.ResourceNotFoundException;
import vdt.kpimanagement.repository.KpiCycleRepo;
import vdt.kpimanagement.repository.KpiDocumentRepo;
import vdt.kpimanagement.repository.EmployeeRepo;
import vdt.kpimanagement.repository.DepartmentRepo;

@Service
public class KpiDocumentService {
    @PersistenceContext
    private EntityManager entityManager;

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
    public KpiDocumentDetailDTO saveOrUpdate(KpiDocumentSaveDTO dto, Long currentEmployeeId) {

        // 1. CỔNG VALIDATE CHUNG (Fail-Fast): Kiểm tra sự tồn tại của Chu kỳ KPI
        if (!kpiCycleRepo.existsById(dto.getCycleId())) {
            throw new BadRequestException("Chu kỳ KPI có ID " + dto.getCycleId() + " không tồn tại trong hệ thống");
        }

        // 2. VALIDATE POLYMORPHIC: Kiểm tra đối tượng nhận KPI (Target) tùy theo loại
        validateTargetEntity(dto.getTargetType(), dto.getTargetId());

        // 3. VALIDATE TÀI LIỆU CHA: Nếu có truyền parentDocId
        if (dto.getParentDocId() != null && !kpiDocumentRepo.existsById(dto.getParentDocId())) {
            throw new BadRequestException("Tài liệu KPI cha có ID " + dto.getParentDocId() + " không tồn tại");
        }

        KpiDocument doc;

        // ==========================================
        // PHÂN NHÁNH LOGIC: CẬP NHẬT HOẶC TẠO MỚI
        // ==========================================
        if (dto.getId() != null) {
            doc = kpiDocumentRepo.findById(dto.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy tài liệu KPI với ID: " + dto.getId()));

            // Bảo mật hệ thống: Nếu tài liệu đã duyệt/khóa, cấm không cho sửa nữa
            if (doc.getStatus() != DocumentStatus.DRAFT) {
                throw new BadRequestException("Chỉ có thể chỉnh sửa tài liệu KPI ở trạng thái NHÁP (DRAFT)");
            }
        } else {
            // HÀNH ĐỘNG: TẠO MỚI (CREATE)
            doc = new KpiDocument();
            doc.setDocumentCode(generateKpiDocumentCode(dto.getCycleId())); // Tự động sinh mã tài liệu theo quy tắc
            doc.setStatus(DocumentStatus.DRAFT); // Mặc định ban đầu luôn là DRAFT
            doc.setCreatedBy(entityManager.getReference(Employee.class, currentEmployeeId)); // Gán proxy người tạo
        }

        // ==========================================
        // MAP DỮ LIỆU TỪ DTO SANG ENTITY
        // ==========================================
//        doc.setDocumentCode(dto.getDocumentCode());
        doc.setTargetType(dto.getTargetType());
        doc.setTargetId(dto.getTargetId());
        doc.setSourceType(dto.getSourceType());

        // Tận dụng getReference để lấy Proxy khóa ngoại cứng, tránh câu lệnh SELECT thừa
        doc.setCycle(entityManager.getReference(KpiCycle.class, dto.getCycleId()));

        if (dto.getParentDocId() != null) {
            doc.setParentDocument(entityManager.getReference(KpiDocument.class, dto.getParentDocId()));
        } else {
            doc.setParentDocument(null);
        }

        // Lưu xuống DB (Hibernate tự nhận biết INSERT hay UPDATE dựa trên việc 'doc' có ID hay chưa)
        KpiDocument savedDoc = kpiDocumentRepo.save(doc);

        // Trả về DTO Detail chuẩn chỉ cho Front-end hiển thị lại Form
        return mapToDetailResponse(savedDoc);
    }

    private void validateTargetEntity(DocumentTargetType type, Long targetId) throws BadRequestException {
        if (type == DocumentTargetType.COMPANY) {
            return; // Cấp công ty không cần check targetId (hoặc mặc định hợp lệ)
        }

        if (targetId == null) {
            throw new BadRequestException("ID đối tượng nhận KPI bắt buộc phải có đối với loại: " + type);
        }

        boolean exists = switch (type) {
            case DEPARTMENT -> departmentRepo.existsById(targetId);
            case EMPLOYEE -> employeeRepo.existsById(targetId);
            default -> false;
        };

        if (!exists) {
            throw new BadRequestException("Đối tượng nhận KPI loại [" + type + "] với ID [" + targetId + "] không tồn tại");
        }
    }

    /**
     * Hàm hỗ trợ map thủ công từ Entity sang Detail DTO (Tránh lỗi Lazy và kiểm soát đầu ra)
     */
    private KpiDocumentDetailDTO mapToDetailResponse(KpiDocument entity) {
        KpiDocumentDetailDTO res = new KpiDocumentDetailDTO();
        res.setId(entity.getId());
        res.setDocumentCode(entity.getDocumentCode());
        res.setTargetType(entity.getTargetType());
        res.setTargetId(entity.getTargetId());
        res.setSourceType(entity.getSourceType());
        res.setStatus(entity.getStatus());
        res.setCreatedAt(entity.getCreatedAt());
        res.setSubmittedAt(entity.getSubmittedAt());
        res.setApprovedAt(entity.getApprovedAt());
        res.setClosedAt(entity.getClosedAt());

        // Lấy thông tin phẳng từ mối quan hệ Cycle (Hibernate tự động load vì session còn mở)
        if (entity.getCycle() != null) {
            res.setCycleId(entity.getCycle().getId());
            res.setCycleName(entity.getCycle().getName());
        }

        // Lấy thông tin phẳng tài liệu cha
        if (entity.getParentDocument() != null) {
            res.setParentDocId(entity.getParentDocument().getId());
            res.setParentDocCode(entity.getParentDocument().getDocumentCode());
        }

        // Lấy thông tin phẳng Người tạo
        if (entity.getCreatedBy() != null) {
            res.setCreatedById(entity.getCreatedBy().getId());
            res.setCreatedByFullName(entity.getCreatedBy().getFullName());
        }

        // Lấy thông tin phẳng Người duyệt (Có thể null nếu đang ở trạng thái DRAFT)
        if (entity.getApprover() != null) {
            res.setApproverId(entity.getApprover().getId());
            res.setApproverFullName(entity.getApprover().getFullName());
        }

        // Xử lý điền tên Target Name (Vì database không có FK nên phải lấy thủ công theo loại)
        if (entity.getTargetType() == DocumentTargetType.DEPARTMENT && entity.getTargetId() != null) {
            departmentRepo.findById(entity.getTargetId())
                    .ifPresent(dept -> res.setTargetName(dept.getName()));
        } else if (entity.getTargetType() == DocumentTargetType.EMPLOYEE && entity.getTargetId() != null) {
            employeeRepo.findById(entity.getTargetId())
                    .ifPresent(emp -> res.setTargetName(emp.getFullName()));
        } else {
            res.setTargetName("Toàn Công Ty");
        }

        return res;
    }

    private String generateKpiDocumentCode(Long cycleId) {
        // 1. Lấy thông tin chu kỳ để lấy mã hoặc năm (Ví dụ: "Q2-2026")
        KpiCycle cycle = kpiCycleRepo.findById(cycleId).orElseThrow();
        String cycleCode = cycle.getCycleCode(); // Giả sử entity KpiCycle có trường code kiểu "Q2-2026"

        // 2. Đếm số lượng tài liệu đã có trong chu kỳ này để làm số tự tăng
        long count = kpiDocumentRepo.countByCycle_IdAndIsDeletedFalse(cycleId);
        long nextNumber = count + 1;

        // 3. Format chuỗi số tự tăng thành 4 ký tự (ví dụ: 1 -> "0001")
        String formattedNumber = String.format("%04d", nextNumber);

        // 4. Ghép lại thành mã hoàn chỉnh
        return "KPI-" + cycleCode + "-" + formattedNumber; // Output: KPI-Q2-2026-0001
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
