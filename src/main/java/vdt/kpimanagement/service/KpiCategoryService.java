package vdt.kpimanagement.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vdt.kpimanagement.dto.KpiCategoryRequest;
import vdt.kpimanagement.dto.KpiCategoryResponse;
import vdt.kpimanagement.entity.KpiCategory;
import vdt.kpimanagement.mapper.KpiCategoryMapper;
import vdt.kpimanagement.repository.KpiCategoryRepo;
import vdt.kpimanagement.repository.KpiTemplateRepo;

@Service
public class KpiCategoryService extends BaseService<KpiCategory, KpiCategoryRequest, KpiCategoryResponse, Long> {

    private final KpiCategoryRepo kpiCategoryRepo;
    private final KpiTemplateRepo kpiTemplateRepo;

    public KpiCategoryService(KpiCategoryRepo kpiCategoryRepo, KpiTemplateRepo kpiTemplateRepo,
                              KpiCategoryMapper kpiCategoryMapper) {
        super(kpiCategoryRepo, kpiCategoryMapper);
        this.kpiCategoryRepo = kpiCategoryRepo;
        this.kpiTemplateRepo = kpiTemplateRepo;
    }

    @Override
    public Page<KpiCategoryResponse> search(String keyword, Pageable pageable) {
        return kpiCategoryRepo.searchByKeyword(keyword, pageable).map(mapper::toDto);
    }

    @Override
    public KpiCategoryResponse create(KpiCategoryRequest request) {
        KpiCategory entity = mapper.toEntity(request);
        if (request.getCategoryCode() == null || request.getCategoryCode().trim().isEmpty()) {
            entity.setCategoryCode(generateCategoryCode());
        } else {
            if (kpiCategoryRepo.existsByCategoryCodeAndIsDeletedFalse(request.getCategoryCode())) {
                throw new IllegalArgumentException("Mã danh mục đã tồn tại: " + request.getCategoryCode());
            }
        }
        entity.setDeleted(false);
        return mapper.toDto(kpiCategoryRepo.save(entity));
    }

    private String generateCategoryCode() {
        long nextNum = kpiCategoryRepo.count() + 1;
        String code;
        do {
            code = String.format("CAT%03d", nextNum++);
        } while (kpiCategoryRepo.existsByCategoryCodeAndIsDeletedFalse(code));
        return code;
    }

    @Override
    public void delete(Long id) {
        // Kiểm tra còn template đang dùng không
        boolean hasActiveTemplates = !kpiTemplateRepo
                .findByCategory_IdAndIsActiveTrueAndIsDeletedFalse(id).isEmpty();
        if (hasActiveTemplates) {
            throw new IllegalArgumentException("Không thể xoá danh mục vì còn tiêu chí mẫu đang hoạt động");
        }
        super.delete(id);
    }
}
