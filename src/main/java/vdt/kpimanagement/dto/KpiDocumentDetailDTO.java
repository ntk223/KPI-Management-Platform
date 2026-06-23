package vdt.kpimanagement.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import vdt.kpimanagement.constant.enums.DocumentStatus;
import vdt.kpimanagement.constant.enums.DocumentTargetType;
import vdt.kpimanagement.constant.enums.SourceType;

import java.time.LocalDateTime;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL) // Trường nào null (như approver khi là DRAFT) sẽ tự ẩn khỏi JSON
public class KpiDocumentDetailDTO {

    private Long id;
    private String documentCode;

    // Thông tin chu kỳ
    private Long cycleId;
    private String cycleName;

    // Thông tin đối tượng nhận KPI
    private DocumentTargetType targetType;
    private Long targetId;
    private String targetName;

    // Thông tin tài liệu cha (nếu có)
    private Long parentDocId;
    private String parentDocCode;

    private SourceType sourceType;
    private DocumentStatus status;

    // Thông tin người tạo
    private Long createdById;
    private String createdByFullName;

    // Thông tin người duyệt (Sẽ ẩn nếu null)
    private Long approverId;
    private String approverFullName;

    // Các mốc thời gian hệ thống
    private LocalDateTime createdAt;
    private LocalDateTime submittedAt;
    private LocalDateTime approvedAt;
    private LocalDateTime closedAt;

    // Mở rộng về sau: Bạn có thể thêm List<KpiItemResponse> kpiItems tại đây
    // để trả về danh sách các tiêu chí KPI chi tiết nằm bên trong tài liệu này.
}