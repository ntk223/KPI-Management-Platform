package vdt.kpimanagement.service;

import org.springframework.stereotype.Service;
import vdt.kpimanagement.dto.PositionRequest;
import vdt.kpimanagement.dto.PositionResponse;
import vdt.kpimanagement.entity.Position;
import vdt.kpimanagement.mapper.PositionMapper;
import vdt.kpimanagement.repository.PositionRepo;

@Service
public class PositionService extends BaseService<Position, PositionRequest, PositionResponse, Long> {

    private final PositionRepo positionRepo;

    public PositionService(PositionRepo positionRepo, PositionMapper positionMapper) {
        super(positionRepo, positionMapper);
        this.positionRepo = positionRepo;
    }

    @Override
    public PositionResponse create(PositionRequest request) {
        Position entity = mapper.toEntity(request);
        if (request.getPositionCode() == null || request.getPositionCode().trim().isEmpty()) {
            entity.setPositionCode(generatePositionCode());
        } else {
            if (positionRepo.existsByPositionCodeAndIsDeletedFalse(request.getPositionCode())) {
                throw new IllegalArgumentException("Mã chức vụ đã tồn tại: " + request.getPositionCode());
            }
        }
        entity.setDeleted(false);
        return mapper.toDto(positionRepo.save(entity));
    }

    private String generatePositionCode() {
        long nextNum = positionRepo.count() + 1;
        String code;
        do {
            code = String.format("POS%03d", nextNum++);
        } while (positionRepo.existsByPositionCodeAndIsDeletedFalse(code));
        return code;
    }
}
