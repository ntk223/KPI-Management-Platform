package vdt.kpimanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class KpiAttachmentUploadDTO {
	private String fileName;
	private String objectKey;
	// private String description;
	private String fileType;
	private Long fileSize;
	private Long kpiItemId;
	private String uploadedBy; // set from security context on backend
}
