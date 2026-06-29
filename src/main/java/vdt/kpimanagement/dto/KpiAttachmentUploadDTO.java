package vdt.kpimanagement.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class KpiAttachmentUploadDTO {
    @NotBlank(message = "Tên tệp không được để trống")
    private String fileName;

    @NotBlank(message = "Đường dẫn lưu trữ không được để trống")
    private String objectKey;

    @NotBlank(message = "Định dạng tệp không được để trống")
    private String fileType;

    @NotNull(message = "Kích thước tệp không được để trống")
    @Min(value = 1, message = "Kích thước tệp phải lớn hơn 0")
    private Long fileSize;

    @NotNull(message = "ID tiêu chí KPI là bắt buộc")
    private Long kpiItemId;

    private String uploadedBy; // set from security context on backend
}
