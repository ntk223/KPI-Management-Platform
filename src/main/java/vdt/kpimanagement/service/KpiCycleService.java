package vdt.kpimanagement.service;

import org.springframework.stereotype.Service;
import vdt.kpimanagement.constant.enums.CycleStatus;
import vdt.kpimanagement.dto.KpiCycleRequest;
import vdt.kpimanagement.dto.KpiCycleResponse;
import vdt.kpimanagement.entity.KpiCycle;
import vdt.kpimanagement.exception.ResourceNotFoundException;
import vdt.kpimanagement.mapper.KpiCycleMapper;
import vdt.kpimanagement.repository.KpiCycleRepo;

import java.util.Map;

@Service
public class KpiCycleService extends BaseService<KpiCycle, KpiCycleRequest, KpiCycleResponse, Long> {

    // Luồng trạng thái hợp lệ: PLANNING → ACTIVE → EVALUATING → CLOSED
    private static final Map<CycleStatus, CycleStatus> VALID_TRANSITIONS = Map.of(
            CycleStatus.PLANNING, CycleStatus.ACTIVE,
            CycleStatus.ACTIVE, CycleStatus.EVALUATING,
            CycleStatus.EVALUATING, CycleStatus.CLOSED
    );

    private final KpiCycleRepo kpiCycleRepo;

    public KpiCycleService(KpiCycleRepo kpiCycleRepo, KpiCycleMapper kpiCycleMapper) {
        super(kpiCycleRepo, kpiCycleMapper);
        this.kpiCycleRepo = kpiCycleRepo;
    }

    @Override
    public KpiCycleResponse create(KpiCycleRequest request) {
        if (request.getStartDate() == null || request.getEndDate() == null
                || !request.getEndDate().isAfter(request.getStartDate())) {
            throw new IllegalArgumentException("Ngày kết thúc phải sau ngày bắt đầu");
        }
        KpiCycle entity = mapper.toEntity(request);
        if (request.getCycleCode() == null || request.getCycleCode().trim().isEmpty()) {
            entity.setCycleCode(generateCycleCode(request));
        } else {
            if (kpiCycleRepo.findByCycleCodeAndIsDeletedFalse(request.getCycleCode()).isPresent()) {
                throw new IllegalArgumentException("Mã chu kỳ đã tồn tại: " + request.getCycleCode());
            }
        }
        entity.setStatus(CycleStatus.PLANNING);
        entity.setDeleted(false);
        return mapper.toDto(kpiCycleRepo.save(entity));
    }

    private String generateCycleCode(KpiCycleRequest request) {
        int year = request.getStartDate() != null ? request.getStartDate().getYear() : java.time.LocalDate.now().getYear();
        long nextNum = kpiCycleRepo.count() + 1;
        String code;
        do {
            code = String.format("CYC-%d-%03d", year, nextNum++);
        } while (kpiCycleRepo.findByCycleCodeAndIsDeletedFalse(code).isPresent());
        return code;
    }

    // Chuyển trạng thái: PLANNING → ACTIVE → EVALUATING → CLOSED
    public KpiCycleResponse changeStatus(Long id, CycleStatus newStatus) {
        KpiCycle cycle = kpiCycleRepo.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy chu kỳ KPI với ID: " + id));
        CycleStatus expected = VALID_TRANSITIONS.get(cycle.getStatus());
        if (expected == null || expected != newStatus) {
            throw new IllegalArgumentException(
                    String.format("Không thể chuyển từ %s sang %s", cycle.getStatus(), newStatus));
        }
        cycle.setStatus(newStatus);
        return mapper.toDto(kpiCycleRepo.save(cycle));
    }
}
