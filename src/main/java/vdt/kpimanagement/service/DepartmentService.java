package vdt.kpimanagement.service;

import org.springframework.stereotype.Service;
import vdt.kpimanagement.repository.DepartmentRepo;
import vdt.kpimanagement.repository.EmployeeRepo;

@Service
public class DepartmentService {

    private final DepartmentRepo departmentRepo;
    private final EmployeeRepo employeeRepo;

    public DepartmentService(DepartmentRepo departmentRepo, EmployeeRepo employeeRepo) {
        this.departmentRepo = departmentRepo;
        this.employeeRepo = employeeRepo;
    }

    // Lấy danh sách tất cả phòng ban (cấp gốc)
    public Object getAll() {
        // TODO: trả về danh sách phòng ban dạng cây hoặc phẳng
        throw new UnsupportedOperationException("Chưa implement");
    }

    // Lấy chi tiết 1 phòng ban theo id
    public Object getById(Long id) {
        // TODO: tìm phòng ban, ném 404 nếu không tồn tại
        throw new UnsupportedOperationException("Chưa implement");
    }

    // Tạo mới phòng ban
    public Object create(Object request) {
        // TODO: validate, kiểm tra mã trùng, lưu DB
        throw new UnsupportedOperationException("Chưa implement");
    }

    // Cập nhật thông tin phòng ban
    public Object update(Long id, Object request) {
        // TODO: validate, cập nhật tên, parent, manager
        throw new UnsupportedOperationException("Chưa implement");
    }

    // Xoá mềm phòng ban
    public void delete(Long id) {
        // TODO: kiểm tra còn nhân viên không trước khi xóa
        throw new UnsupportedOperationException("Chưa implement");
    }
}
