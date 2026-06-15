package vdt.kpimanagement.service;

import org.springframework.stereotype.Service;
import vdt.kpimanagement.constant.enums.CycleStatus;
import vdt.kpimanagement.repository.KpiCycleRepo;

@Service
public class KpiCycleService {

    private final KpiCycleRepo kpiCycleRepo;

    public KpiCycleService(KpiCycleRepo kpiCycleRepo) {
        this.kpiCycleRepo = kpiCycleRepo;
    }

    // Lấy danh sách tất cả chu kỳ
    public Object getAll() {
        // TODO: trả về list KpiCycleDTO
        throw new UnsupportedOperationException("Chưa implement");
    }

    // Lấy chi tiết 1 chu kỳ
    public Object getById(Long id) {
        // TODO: tìm chu kỳ, ném 404 nếu không tồn tại
        throw new UnsupportedOperationException("Chưa implement");
    }

    // Tạo chu kỳ mới (chỉ ADMIN)
    public Object create(Object request) {
        // TODO: validate ngày hợp lệ, mã không trùng, status = PLANNING
        throw new UnsupportedOperationException("Chưa implement");
    }

    // Cập nhật thông tin chu kỳ (chỉ khi status = PLANNING)
    public Object update(Long id, Object request) {
        // TODO: chỉ cho sửa khi còn ở PLANNING
        throw new UnsupportedOperationException("Chưa implement");
    }

    // Chuyển trạng thái chu kỳ (chỉ ADMIN)
    // PLANNING → ACTIVE → EVALUATING → CLOSED
    public Object changeStatus(Long id, CycleStatus newStatus) {
        // TODO: validate luồng trạng thái hợp lệ
        throw new UnsupportedOperationException("Chưa implement");
    }
}
