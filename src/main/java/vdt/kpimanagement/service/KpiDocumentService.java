package vdt.kpimanagement.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import vdt.kpimanagement.constant.enums.DocumentStatus;
import vdt.kpimanagement.constant.enums.DocumentTargetType;
import vdt.kpimanagement.dto.KpiDocumentDetailDTO;
import vdt.kpimanagement.dto.KpiDocumentSaveDTO;
import vdt.kpimanagement.dto.KpiDocumentSearchDTO;
import vdt.kpimanagement.dto.KpiItemDTO;
import vdt.kpimanagement.entity.Employee;
import vdt.kpimanagement.entity.KpiCycle;
import vdt.kpimanagement.entity.KpiDocument;
import vdt.kpimanagement.entity.KpiItem;
import vdt.kpimanagement.entity.KpiTemplate;
import vdt.kpimanagement.exception.BadRequestException;
import vdt.kpimanagement.exception.ResourceNotFoundException;
import vdt.kpimanagement.repository.*;

import org.springframework.security.core.GrantedAuthority;
import vdt.kpimanagement.entity.Account;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class KpiDocumentService {
    @PersistenceContext
    private EntityManager entityManager;

    private final KpiDocumentRepo kpiDocumentRepo;
    private final KpiCycleRepo kpiCycleRepo;
    private final EmployeeRepo employeeRepo;
    private final DepartmentRepo departmentRepo;
    private final KpiItemRepo kpiItemRepo;
    private final AccountRepo accountRepo;
    private final KpiItemEvaluationRepo kpiItemEvaluationRepo;

    public KpiDocumentService(KpiDocumentRepo kpiDocumentRepo,
                              KpiCycleRepo kpiCycleRepo,
                              EmployeeRepo employeeRepo,
                              DepartmentRepo departmentRepo,
                              KpiItemRepo kpiItemRepo,
                              AccountRepo accountRepo,
                              KpiItemEvaluationRepo kpiItemEvaluationRepo) {
        this.kpiDocumentRepo = kpiDocumentRepo;
        this.kpiCycleRepo = kpiCycleRepo;
        this.employeeRepo = employeeRepo;
        this.departmentRepo = departmentRepo;
        this.kpiItemRepo = kpiItemRepo;
        this.accountRepo = accountRepo;
        this.kpiItemEvaluationRepo = kpiItemEvaluationRepo;
    }

    // lay theo type giám đốc
//    public KpiDocumentDetailDTO getDocByType(String type) {
//
//    }

    public List<KpiDocumentDetailDTO> search(KpiDocumentSearchDTO searchDTO) {
        // 1. Câu lệnh 1: Lấy danh sách Documents dựa trên filter (Dùng Specification hoặc Native Query tùy bạn)
        List<KpiDocument> documents = kpiDocumentRepo.searchDocuments(
                searchDTO.getDocumentCode(),
                searchDTO.getCycleId(),
                searchDTO.getTargetType(),
                searchDTO.getTargetId(),
                searchDTO.getStatus()
        );

        if (documents.isEmpty()) {
            return new ArrayList<>();
        }

        // 2. Gom tất cả Document ID lại thành một List
        List<Long> docIds = documents.stream()
                .map(KpiDocument::getId)
                .collect(Collectors.toList());

        // 3. Câu lệnh 2: Bắn DUY NHẤT 1 phát súng xuống DB để lấy sạch Items của tất cả các Doc này
        // SQL ngầm: SELECT * FROM kpi_items WHERE document_id IN (1, 2, 3, ...) AND is_deleted = false
        List<KpiItem> allItems = kpiItemRepo.findByDocumentIdInAndIsDeletedFalse(docIds);

        // 4. Nhóm các Item lại theo từng Document ID bằng Map để tìm kiếm với tốc độ O(1)
        Map<Long, List<KpiItem>> itemsByDocIdMap = allItems.stream()
                .collect(Collectors.groupingBy(item -> item.getDocument().getId()));

        // 5. Map sang DTO phẳng, sạch sẽ mà không phải gọi thêm bất kỳ câu lệnh SQL nào trong vòng lặp
        List<KpiDocumentDetailDTO> dtos = new ArrayList<>();
        for (KpiDocument doc : documents) {
            // Lấy list item từ Map ra, nếu không có thì trả về list rỗng tránh lỗi NullPointerException
            List<KpiItem> docItems = itemsByDocIdMap.getOrDefault(doc.getId(), new ArrayList<>());

            // Hàm mapToDetailResponse này sẽ tự tính toán % và điểm động như ta đã bàn
            dtos.add(mapToDetailResponse(doc, docItems));
        }

        return dtos;
    }

    // Lấy chi tiết phiếu KPI theo ID (đúng role/permission)
    public KpiDocumentDetailDTO getById(Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new BadRequestException("Không xác thực được người dùng");
        }
        String username = authentication.getName();
        var authorities = authentication.getAuthorities();

        KpiDocument doc = kpiDocumentRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy tài liệu KPI với ID: " + id));

        // Kiểm tra quyền truy cập dựa trên Role
        boolean isAdmin = authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        boolean isDirector = authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_DIRECTOR"));

        if (!isAdmin && !isDirector) {
            Account account = accountRepo.findByUsername(username)
                    .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy tài khoản người dùng: " + username));
            Employee employee = account.getEmployee();
            Long empDeptId = employee.getDepartment() != null ? employee.getDepartment().getId() : null;

            boolean isManager = authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_MANAGER"));

            if (isManager) {
                // Quản lý: chỉ được xem KPI của phòng ban mình, hoặc KPI của nhân viên thuộc phòng ban mình
                if (doc.getTargetType() == DocumentTargetType.DEPARTMENT) {
                    if (!doc.getTargetId().equals(empDeptId)) {
                        throw new BadRequestException("Bạn không có quyền xem KPI của phòng ban khác");
                    }
                } else if (doc.getTargetType() == DocumentTargetType.EMPLOYEE) {
                    Employee targetEmployee = employeeRepo.findById(doc.getTargetId())
                            .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy nhân viên nhận KPI"));
                    Long targetDeptId = targetEmployee.getDepartment() != null ? targetEmployee.getDepartment().getId() : null;
                    if (targetDeptId == null || !targetDeptId.equals(empDeptId)) {
                        throw new BadRequestException("Bạn không có quyền xem KPI của nhân viên thuộc phòng ban khác");
                    }
                }
            } else {
                // Nhân viên thường: chỉ được xem KPI của phòng ban mình, hoặc KPI cá nhân của chính mình
                if (doc.getTargetType() == DocumentTargetType.DEPARTMENT) {
                    if (!doc.getTargetId().equals(empDeptId)) {
                        throw new BadRequestException("Bạn không có quyền xem KPI của phòng ban khác");
                    }
                } else if (doc.getTargetType() == DocumentTargetType.EMPLOYEE) {
                    if (!doc.getTargetId().equals(employee.getId())) {
                        throw new BadRequestException("Bạn không có quyền xem KPI của nhân viên khác");
                    }
                }
            }
        }

        List<KpiItem> items = kpiItemRepo.findByDocument_IdAndIsDeletedFalse(doc.getId());
        return mapToDetailResponse(doc, items);
    }

    private void checkTargetPermission(DocumentTargetType targetType, Long targetId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new BadRequestException("Không xác thực được người dùng");
        }
        String username = authentication.getName();
        var authorities = authentication.getAuthorities();

        boolean isAdmin = authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        boolean isDirector = authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_DIRECTOR"));

        if (!isAdmin && !isDirector) {
            if (targetType != DocumentTargetType.COMPANY && targetId == null) {
                throw new BadRequestException("Bắt buộc phải cung cấp ID đối tượng nhận KPI");
            }
            Account account = accountRepo.findByUsername(username)
                    .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy tài khoản người dùng: " + username));
            Employee employee = account.getEmployee();
            Long empDeptId = employee.getDepartment() != null ? employee.getDepartment().getId() : null;

            if (targetType == DocumentTargetType.DEPARTMENT) {
                if (!targetId.equals(empDeptId)) {
                    throw new BadRequestException("Bạn không có quyền xem KPI của phòng ban khác");
                }
            } else if (targetType == DocumentTargetType.EMPLOYEE) {
                boolean isManager = authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_MANAGER"));
                if (isManager) {
                    Employee targetEmployee = employeeRepo.findById(targetId)
                            .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy nhân viên nhận KPI"));
                    Long targetDeptId = targetEmployee.getDepartment() != null ? targetEmployee.getDepartment().getId() : null;
                    if (targetDeptId == null || !targetDeptId.equals(empDeptId)) {
                        throw new BadRequestException("Bạn không có quyền xem KPI của nhân viên thuộc phòng ban khác");
                    }
                } else {
                    if (!targetId.equals(employee.getId())) {
                        throw new BadRequestException("Bạn không có quyền xem KPI của nhân viên khác");
                    }
                }
            }
        }
    }

    // Lấy KPI theo target (COMPANY / DEPARTMENT / EMPLOYEE) trong 1 chu kỳ
    public List<KpiDocumentDetailDTO> getByTarget(Long cycleId, DocumentTargetType targetType, Long targetId) {
        checkTargetPermission(targetType, targetId);

        List<KpiDocument> docs;
        if (targetId != null) {
            docs = kpiDocumentRepo.findByCycle_IdAndTargetTypeAndTargetIdAndIsDeletedFalse(cycleId, targetType, targetId)
                    .map(List::of)
                    .orElse(List.of());
        } else {
            docs = kpiDocumentRepo.findByCycle_IdAndTargetTypeAndIsDeletedFalse(cycleId, targetType);
        }

        List<KpiDocumentDetailDTO> dtos = new ArrayList<>();
        for (KpiDocument doc : docs) {
            List<KpiItem> items = kpiItemRepo.findByDocument_IdAndIsDeletedFalse(doc.getId());
            dtos.add(mapToDetailResponse(doc, items));
        }
        return dtos;
    }

    // Lấy KPI cá nhân của nhân viên đang đăng nhập
    public KpiDocumentDetailDTO getMyDocument(Long cycleId, Long employeeId) {
        checkTargetPermission(DocumentTargetType.EMPLOYEE, employeeId);

        KpiDocument doc = kpiDocumentRepo.findByCycle_IdAndTargetTypeAndTargetIdAndIsDeletedFalse(
                cycleId, DocumentTargetType.EMPLOYEE, employeeId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Không tìm thấy phiếu KPI cá nhân cho nhân viên ID " + employeeId + " trong chu kỳ " + cycleId));
        List<KpiItem> items = kpiItemRepo.findByDocument_IdAndIsDeletedFalse(doc.getId());
        return mapToDetailResponse(doc, items);
    }

    // Tạo phiếu KPI mới (DIRECTOR/MANAGER tạo, hoặc EMPLOYEE đề xuất)
    public Object create(Object request) {
        // TODO: validate cycle đang ACTIVE
        // TODO: validate target_type + target_id hợp lệ (polymorphic check)
        // TODO: kiểm tra unique (cycle_id, target_type, target_id)
        // TODO: set status = DRAFT, source_type theo người tạo
        throw new UnsupportedOperationException("Chưa implement");
    }
    public KpiDocumentDetailDTO saveOrUpdate(KpiDocumentSaveDTO dto, String username) {

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
            doc.setCreatedBy(username); // Gán proxy người tạo
        }

        // ==========================================
        // MAP DỮ LIỆU TỪ DTO SANG ENTITY
        // ==========================================
//        doc.setDocumentCode(dto.getDocumentCode());
        doc.setTargetType(dto.getTargetType()); // Polymorphic: COMPANY / DEPARTMENT / EMPLOYEE
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

        List<KpiItem> itemsToSave = new ArrayList<>();
        List<KpiItemDTO> itemDTOS = dto.getKpiItems();

        if (dto.getId() != null) {
            // Trường hợp UPDATE:
            List<KpiItem> existingItems = kpiItemRepo.findByDocument_IdAndIsDeletedFalse(savedDoc.getId());
            Map<Long, KpiItem> existingItemMap = new HashMap<>();
            for (KpiItem item : existingItems) {
                existingItemMap.put(item.getId(), item);
            }

            if (itemDTOS != null) {
                for (KpiItemDTO itemDTO : itemDTOS) {
                    KpiItem item;
                    if (itemDTO.getId() != null && existingItemMap.containsKey(itemDTO.getId())) {
                        // Update existing item
                        item = existingItemMap.remove(itemDTO.getId());
                    } else {
                        // Create new item
                        item = new KpiItem();
                        item.setDocument(savedDoc);
                    }
                    item.setName(itemDTO.getName());
                    item.setDescription(itemDTO.getDescription());
                    item.setUnit(itemDTO.getUnit());
                    item.setTargetType(itemDTO.getTargetType());
                    item.setTargetValue(itemDTO.getTargetValue());
                    item.setWeight(itemDTO.getWeight());
                    if (itemDTO.getTemplateId() != null) {
                        item.setTemplate(entityManager.getReference(KpiTemplate.class, itemDTO.getTemplateId()));
                    } else {
                        item.setTemplate(null);
                    }
                    item.setDeleted(false);
                    itemsToSave.add(item);
                }
            }

            // Những item còn lại trong map tức là bị xóa ở frontend
            for (KpiItem removedItem : existingItemMap.values()) {
                removedItem.setDeleted(true);
                itemsToSave.add(removedItem);
            }
        } else {
            // Trường hợp CREATE mới hoàn toàn:
            if (itemDTOS != null) {
                for (KpiItemDTO itemDTO : itemDTOS) {
                    KpiItem item = new KpiItem();
                    item.setDocument(savedDoc);
                    item.setName(itemDTO.getName());
                    item.setDescription(itemDTO.getDescription());
                    item.setUnit(itemDTO.getUnit());
                    item.setTargetType(itemDTO.getTargetType());
                    item.setTargetValue(itemDTO.getTargetValue());
                    item.setWeight(itemDTO.getWeight());
                    if (itemDTO.getTemplateId() != null) {
                        item.setTemplate(entityManager.getReference(KpiTemplate.class, itemDTO.getTemplateId()));
                    } else {
                        item.setTemplate(null);
                    }
                    item.setDeleted(false);
                    itemsToSave.add(item);
                }
            }
        }

        kpiItemRepo.saveAll(itemsToSave);

        // Lấy lại danh sách active items để map trả về response
        List<KpiItem> activeItems = new ArrayList<>();
        for (KpiItem item : itemsToSave) {
            if (!item.isDeleted()) {
                activeItems.add(item);
            }
        }

        // Trả về DTO Detail chuẩn chỉ cho Front-end hiển thị lại Form
        return mapToDetailResponse(savedDoc, activeItems);
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
    private KpiDocumentDetailDTO mapToDetailResponse(KpiDocument entity, List<KpiItem> items) {
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
            res.setCreatedBy(entity.getCreatedBy());
        }
        if  (entity.getApprovedBy() != null) {
            res.setApprovedBy(entity.getApprovedBy());
        }
        // Lấy thông tin phẳng Người duyệt (Có thể null nếu đang ở trạng thái DRAFT)
//        if (entity.getApprover() != null) {
//            res.setApproverId(entity.getApprover().getId());
//            res.setApproverFullName(entity.getApprover().getFullName());
//        }

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

        List<KpiItemDTO> itemDTOS = new ArrayList<>();
        if (items != null) {
            for (KpiItem item  : items) {
                KpiItemDTO itemDTO = new KpiItemDTO();
                itemDTO.setId(item.getId());
                itemDTO.setName(item.getName());
                itemDTO.setDescription(item.getDescription());
                itemDTO.setUnit(item.getUnit());
                itemDTO.setTargetType(item.getTargetType());
                itemDTO.setWeight(item.getWeight());
                itemDTO.setTargetValue(item.getTargetValue());
                itemDTO.setCurrentValue(item.getCurrentValue());
                if (item.getTemplate() != null) {
                    itemDTO.setTemplateId(item.getTemplate().getId());
                }

                kpiItemEvaluationRepo.findByKpiItem_IdAndIsDeletedFalse(item.getId())
                        .ifPresent(eval -> {
                            itemDTO.setSelfScore(eval.getSelfScore());
                            itemDTO.setManagerScore(eval.getManagerScore());
                            itemDTO.setFinalScore(eval.getFinalScore());
                        });

                itemDTOS.add(itemDTO);
            }
        }
        res.setKpiItems(itemDTOS);

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
