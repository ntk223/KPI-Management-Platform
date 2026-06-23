package vdt.kpimanagement.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    public Page<PositionResponse> search(String keyword, Pageable pageable) {
        return positionRepo.searchByKeyword(keyword, pageable).map(mapper::toDto);
    }

    @Override
    public PositionResponse create(PositionRequest request) {
        if (positionRepo.existsByPositionCodeAndIsDeletedFalse(request.getPositionCode())) {
            throw new IllegalArgumentException("Mã chức vụ đã tồn tại: " + request.getPositionCode());
        }
        return super.create(request);
    }
}
