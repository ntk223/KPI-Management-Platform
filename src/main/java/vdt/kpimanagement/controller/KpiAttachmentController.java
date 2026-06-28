package vdt.kpimanagement.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import vdt.kpimanagement.dto.ApiResponse;
import vdt.kpimanagement.dto.KpiAttachmentUploadDTO;
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

    /** Step 1: Request a pre-signed PUT URL from S3 */
    @PostMapping("/request-upload")
    public ApiResponse<Object> requestUpload(@RequestParam("fileName") String fileName) {
        return ApiResponse.success(200, "Upload request received",
                fileStorageService.generatePresignedUrl(fileName));
    }

    /** Step 3: Confirm upload — saves metadata to DB */
    @PostMapping("/confirm-upload")
    public ApiResponse<Object> confirmUpload(
            @RequestBody KpiAttachmentUploadDTO request,
            @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails != null) {
            request.setUploadedBy(userDetails.getUsername());
        }
        return ApiResponse.success(200, "Upload confirmed", kpiAttachmentService.save(request));
    }

    /** List all attachments for a KPI item */
    @GetMapping("/kpi-item/{kpiItemId}")
    public ApiResponse<Object> listByKpiItem(@PathVariable Long kpiItemId) {
        return ApiResponse.success(200, "Attachments fetched",
                kpiAttachmentService.listByKpiItemId(kpiItemId));
    }

    /** Generate a pre-signed GET URL so the manager can download the file */
    @GetMapping("/{id}/download-url")
    public ApiResponse<Object> getDownloadUrl(@PathVariable Long id) {
        String objectKey = kpiAttachmentService.getObjectKey(id);
        String url = fileStorageService.generateDownloadUrl(objectKey);
        return ApiResponse.success(200, "Download URL generated", url);
    }

    /** Soft-delete an attachment */
    @DeleteMapping("/{id}")
    public ApiResponse<Object> deleteAttachment(@PathVariable Long id) {
        kpiAttachmentService.delete(id);
        return ApiResponse.success(200, "Attachment deleted", null);
    }
}
