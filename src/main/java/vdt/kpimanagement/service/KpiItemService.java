package vdt.kpimanagement.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vdt.kpimanagement.constant.enums.KpiItemType;
import vdt.kpimanagement.entity.KpiDocument;
import vdt.kpimanagement.entity.KpiItem;
import vdt.kpimanagement.exception.BadRequestException;
import vdt.kpimanagement.exception.ResourceNotFoundException;
import vdt.kpimanagement.repository.KpiItemRepo;
import vdt.kpimanagement.repository.KpiDocumentRepo;
import vdt.kpimanagement.repository.KpiTemplateRepo;

import java.math.BigDecimal;
import java.util.List;

@Service
public class KpiItemService {

    private final KpiItemRepo kpiItemRepo;
    private final KpiDocumentRepo kpiDocumentRepo;
    private final KpiTemplateRepo kpiTemplateRepo;
    private final KpiProgressCalculator progressCalculator;

    public KpiItemService(KpiItemRepo kpiItemRepo,
                          KpiDocumentRepo kpiDocumentRepo,
                          KpiTemplateRepo kpiTemplateRepo,
                          KpiProgressCalculator progressCalculator) {
        this.kpiItemRepo = kpiItemRepo;
        this.kpiDocumentRepo = kpiDocumentRepo;
        this.kpiTemplateRepo = kpiTemplateRepo;
        this.progressCalculator = progressCalculator;

    }

    /**
     * Cập nhật giá trị thực tế cho KpiItem và thực hiện roll-up đệ quy lên cha
     */
    @Transactional
    public void updateItemValue(Long itemId, BigDecimal newValue) {
        KpiItem item = kpiItemRepo.findById(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy tiêu chí KPI với ID: " + itemId));

        // Rule 3: Chặn cập nhật trực tiếp cho item type = GROUP
        if (item.getItemType() == KpiItemType.GROUP) {
            throw new BadRequestException("Không được phép cập nhật trực tiếp tiến độ/giá trị cho tiêu chí loại GROUP.");
        }

        // Cập nhật giá trị hiện tại cho Leaf node
        item.setCurrentValue(newValue);

        // Tính tiến độ (%) cho Leaf node
        BigDecimal leafProgress = progressCalculator.calculateLeafProgress(
                item.getTargetType(),
                item.getCurrentValue(),
                item.getTargetValue()
        );
        item.setProgress(leafProgress);
        kpiItemRepo.save(item);

        // Kích hoạt roll-up đệ quy lên các cấp cha và cập nhật document
        rollup(item);
    }

    /**
     * Hàm đệ quy Roll-up tiến độ lên cấp cha (KpiItem) hoặc cập nhật KpiDocument nếu là node gốc
     */
    private void rollup(KpiItem item) {
        if (item == null) {
            return;
        }

        if (item.getParent() != null) {
            // Roll-up lên item cha
            KpiItem parent = item.getParent();

            // Lấy tất cả con của cha không bị xóa
            List<KpiItem> activeChildren = kpiItemRepo.findByParent_IdAndIsDeletedFalse(parent.getId());

            // Tính tiến độ cho parent GROUP
            BigDecimal parentProgress = progressCalculator.calculateGroupProgress(
                    parent.getAggregationType(),
                    activeChildren
            );
            parent.setProgress(parentProgress);
            kpiItemRepo.save(parent);

            // Tiếp tục đệ quy lên cha của parent
            rollup(parent);
        } else {
            // Gốc (parent == null): Cập nhật điểm tổng kết cho KpiDocument
            KpiDocument doc = item.getDocument();
            if (doc != null) {
                // Lấy tất cả các item gốc của document
                List<KpiItem> rootItems = kpiItemRepo.findByDocument_IdAndIsDeletedFalseAndParentIsNull(doc.getId());

                // Tính overallScore
                BigDecimal overallScore = progressCalculator.calculateOverallScore(rootItems);
                doc.setTotalProgress(overallScore);
                kpiDocumentRepo.save(doc);
            }
        }
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
