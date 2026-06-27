package vdt.kpimanagement.controller;

import org.springframework.web.bind.annotation.*;
import vdt.kpimanagement.dto.ApiResponse;
import vdt.kpimanagement.service.FileStorageService;
import vdt.kpimanagement.service.KpiAttachmentService;

@RestController
@RequestMapping("/kpi-attachments")
public class KpiAttachmentController {
    private final FileStorageService fileStorageService;
    private final KpiAttachmentService kpiAttachmentService;

    public KpiAttachmentController(FileStorageService fileStorageService, KpiAttachmentService kpiAttachmentService) {
        this.fileStorageService = fileStorageService;
        this.kpiAttachmentService = kpiAttachmentService;
    }

    @PostMapping("/request-upload")
    public ApiResponse<Object> requestUpload(@RequestParam("fileName") String fileName) {
        return ApiResponse.success(200, "Upload request received",
                fileStorageService.generatePresignedUrl(fileName));
    }

//    @PostMapping("/confirm-upload")
//    public ResponseEntity<?> confirmUpload(@RequestBody FileConfirmationDTO request) {
//        // Logic kiểm tra file có thực sự tồn tại trên S3 không (optional nhưng khuyên dùng)
//
//        // Lưu thông tin file vào MySQL thông qua Spring Data JPA
//        EvidenceFile evidenceFile = new EvidenceFile();
//        evidenceFile.setOriginalName(request.getOriginalFileName());
//        evidenceFile.setObjectKey(request.getObjectKey()); // Ví dụ: "evidences/uuid.pdf"
//        evidenceFile.setUserId(request.getUserId());
//
//        evidenceFileRepository.save(evidenceFile);
//
//        return ResponseEntity.ok("File metadata saved successfully.");
//    }
}
