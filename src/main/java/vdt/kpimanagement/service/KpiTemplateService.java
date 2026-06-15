package vdt.kpimanagement.service;

import org.springframework.stereotype.Service;
import vdt.kpimanagement.dto.KpiTemplateRequest;
import vdt.kpimanagement.dto.KpiTemplateResponse;
import vdt.kpimanagement.entity.KpiCategory;
import vdt.kpimanagement.entity.KpiTemplate;
import vdt.kpimanagement.exception.ResourceNotFoundException;
import vdt.kpimanagement.mapper.KpiTemplateMapper;
import vdt.kpimanagement.repository.KpiCategoryRepo;
import vdt.kpimanagement.repository.KpiTemplateRepo;

import java.util.List;

@Service
public class KpiTemplateService extends BaseService<KpiTemplate, KpiTemplateRequest, KpiTemplateResponse, Long> {

    private final KpiTemplateRepo kpiTemplateRepo;
    private final KpiCategoryRepo kpiCategoryRepo;

    public KpiTemplateService(KpiTemplateRepo kpiTemplateRepo, KpiCategoryRepo kpiCategoryRepo,
                              KpiTemplateMapper kpiTemplateMapper) {
        super(kpiTemplateRepo, kpiTemplateMapper);
        this.kpiTemplateRepo = kpiTemplateRepo;
        this.kpiCategoryRepo = kpiCategoryRepo;
    }

    @Override
    public KpiTemplateResponse create(KpiTemplateRequest request) {
        if (kpiTemplateRepo.existsByTemplateCodeAndIsDeletedFalse(request.getTemplateCode())) {
            throw new IllegalArgumentException("Mã tiêu chí mẫu đã tồn tại: " + request.getTemplateCode());
        }
        KpiTemplate entity = mapper.toEntity(request);
        entity.setCategory(resolveCategory(request.getCategoryId()));
        entity.setActive(true);
        entity.setDeleted(false);
        return mapper.toDto(kpiTemplateRepo.save(entity));
    }

    @Override
    public KpiTemplateResponse update(Long id, KpiTemplateRequest request) {
        KpiTemplate entity = kpiTemplateRepo.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy tiêu chí mẫu với ID: " + id));
        mapper.updateEntityFromDto(request, entity);
        entity.setCategory(resolveCategory(request.getCategoryId()));
        return mapper.toDto(kpiTemplateRepo.save(entity));
    }

    // Lấy template theo danh mục (chỉ những template active)
    public List<KpiTemplateResponse> getByCategory(Long categoryId) {
        return kpiTemplateRepo.findByCategory_IdAndIsActiveTrueAndIsDeletedFalse(categoryId)
                .stream().map(mapper::toDto).toList();
    }

    // Bật/tắt template
    public void toggleActive(Long id, boolean isActive) {
        KpiTemplate entity = kpiTemplateRepo.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy tiêu chí mẫu với ID: " + id));
        entity.setActive(isActive);
        kpiTemplateRepo.save(entity);
    }

    private KpiCategory resolveCategory(Long categoryId) {
        if (categoryId == null) return null;
        return kpiCategoryRepo.findByIdAndIsDeletedFalse(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy danh mục với ID: " + categoryId));
    }
}
