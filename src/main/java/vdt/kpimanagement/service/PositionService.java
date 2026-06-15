package vdt.kpimanagement.service;

import org.springframework.stereotype.Service;
import vdt.kpimanagement.repository.PositionRepo;

@Service
public class PositionService {

    private final PositionRepo positionRepo;

    public PositionService(PositionRepo positionRepo) {
        this.positionRepo = positionRepo;
    }

    // Lấy danh sách tất cả chức vụ
    public Object getAll() {
        // TODO: trả về list PositionDTO
        throw new UnsupportedOperationException("Chưa implement");
    }

    // Lấy chi tiết 1 chức vụ
    public Object getById(Long id) {
        // TODO: tìm chức vụ, ném 404 nếu không tồn tại
        throw new UnsupportedOperationException("Chưa implement");
    }

    // Tạo mới chức vụ
    public Object create(Object request) {
        // TODO: validate, kiểm tra mã trùng, lưu DB
        throw new UnsupportedOperationException("Chưa implement");
    }

    // Cập nhật chức vụ
    public Object update(Long id, Object request) {
        // TODO: validate, cập nhật title, level
        throw new UnsupportedOperationException("Chưa implement");
    }

    // Xoá mềm chức vụ
    public void delete(Long id) {
        // TODO: kiểm tra còn nhân viên đang dùng không
        throw new UnsupportedOperationException("Chưa implement");
    }
}
