package vdt.kpimanagement.service;

import org.springframework.stereotype.Service;
import vdt.kpimanagement.dto.KpiAttachmentResponseDTO;
import vdt.kpimanagement.dto.KpiAttachmentUploadDTO;
import vdt.kpimanagement.entity.KpiAttachment;
import vdt.kpimanagement.entity.KpiItem;
import vdt.kpimanagement.repository.KpiAttachmentRepo;
import vdt.kpimanagement.repository.KpiItemRepo;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class KpiAttachmentService {
    private final KpiAttachmentRepo kpiAttachmentRepo;
    private final KpiItemRepo kpiItemRepo;

    public KpiAttachmentService(KpiAttachmentRepo kpiAttachmentRepo, KpiItemRepo kpiItemRepo) {
        this.kpiAttachmentRepo = kpiAttachmentRepo;
        this.kpiItemRepo = kpiItemRepo;
    }

    /** Confirm upload: save metadata to DB and return the saved DTO */
    public KpiAttachmentResponseDTO save(KpiAttachmentUploadDTO dto) {
        KpiItem kpiItem = kpiItemRepo.findById(dto.getKpiItemId())
                .orElseThrow(() -> new IllegalArgumentException("KpiItem not found: " + dto.getKpiItemId()));

        KpiAttachment attachment = new KpiAttachment();
        attachment.setKpiItem(kpiItem);
        attachment.setFileName(dto.getFileName());
        attachment.setFileType(dto.getFileType());
        attachment.setObjectKey(dto.getObjectKey());
        attachment.setFileSizeBytes(dto.getFileSize());
        // uploadedBy will be set from security context in the controller layer
        attachment.setUploadedBy(dto.getUploadedBy() != null ? dto.getUploadedBy() : "system");

        KpiAttachment saved = kpiAttachmentRepo.save(attachment);
        return toDTO(saved);
    }

    /** List all attachments for a KPI item */
    public List<KpiAttachmentResponseDTO> listByKpiItemId(Long kpiItemId) {
        return kpiAttachmentRepo.findAllByKpiItem_IdAndIsDeletedFalse(kpiItemId)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    /** Retrieve objectKey for generating a download presigned URL */
    public String getObjectKey(Long attachmentId) {
        KpiAttachment attachment = kpiAttachmentRepo.findById(attachmentId)
                .orElseThrow(() -> new IllegalArgumentException("Attachment not found: " + attachmentId));
        if (attachment.isDeleted()) throw new IllegalArgumentException("Attachment has been deleted");
        return attachment.getObjectKey();
    }

    /** Soft-delete an attachment */
    public void delete(Long attachmentId) {
        KpiAttachment attachment = kpiAttachmentRepo.findById(attachmentId)
                .orElseThrow(() -> new IllegalArgumentException("Attachment not found: " + attachmentId));
        attachment.setDeleted(true);
        kpiAttachmentRepo.save(attachment);
    }

    // ── Mapping ─────────────────────────────────────────────────────────────────
    private KpiAttachmentResponseDTO toDTO(KpiAttachment a) {
        return KpiAttachmentResponseDTO.builder()
                .id(a.getId())
                .kpiItemId(a.getKpiItem() != null ? a.getKpiItem().getId() : null)
                .fileName(a.getFileName())
                .objectKey(a.getObjectKey())
                .fileType(a.getFileType())
                .fileSize(a.getFileSizeBytes())
                .uploadedBy(a.getUploadedBy())
                .uploadedAt(a.getCreatedAt())
                .build();
    }
}

