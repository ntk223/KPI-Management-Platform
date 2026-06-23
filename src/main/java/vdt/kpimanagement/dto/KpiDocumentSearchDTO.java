package vdt.kpimanagement.dto;

import lombok.Getter;
import lombok.Setter;
import vdt.kpimanagement.constant.enums.DocumentStatus;
import vdt.kpimanagement.constant.enums.DocumentTargetType;

@Getter
@Setter
public class KpiDocumentSearchDTO {

    // Các trường lọc dữ liệu (Tất cả đều để Nullable để không bắt buộc lọc)
    private String documentCode;
    private Long cycleId;
    private DocumentTargetType targetType;
    private Long targetId;
    private DocumentStatus status;
    private Long createdBy;

    // Các trường phục vụ phân trang & sắp xếp (Đặt sẵn giá trị mặc định chuẩn)
    private int page = 0;
    private int size = 10;
    private String sortBy = "createdAt";
    private String sortDirection = "DESC"; // DESC để tài liệu mới tạo lên đầu
}