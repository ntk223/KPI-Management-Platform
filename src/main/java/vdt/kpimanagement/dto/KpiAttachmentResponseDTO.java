package vdt.kpimanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class KpiAttachmentResponseDTO {
    private Long id;
    private Long kpiItemId;
    private String fileName;
    private String objectKey;
    private String fileType;
    private Long fileSize;
    private String uploadedBy;
    private LocalDateTime uploadedAt;
}
