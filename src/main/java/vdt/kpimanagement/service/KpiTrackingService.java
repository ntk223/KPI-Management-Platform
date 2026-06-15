package vdt.kpimanagement.service;

import org.springframework.stereotype.Service;
import vdt.kpimanagement.repository.KpiTrackingLogRepo;
import vdt.kpimanagement.repository.KpiItemRepo;
import vdt.kpimanagement.repository.EmployeeRepo;

@Service
public class KpiTrackingService {

    private final KpiTrackingLogRepo kpiTrackingLogRepo;
    private final KpiItemRepo kpiItemRepo;
    private final EmployeeRepo employeeRepo;

    public KpiTrackingService(KpiTrackingLogRepo kpiTrackingLogRepo,
                               KpiItemRepo kpiItemRepo,
                               EmployeeRepo employeeRepo) {
        this.kpiTrackingLogRepo = kpiTrackingLogRepo;
        this.kpiItemRepo = kpiItemRepo;
        this.employeeRepo = employeeRepo;
    }

    // Cập nhật tiến độ 1 tiêu chí KPI
    public Object addProgress(Object request) {
        // TODO: validate cycle.status = ACTIVE, document.status = IN_PROGRESS
        // TODO: tính value_before = kpi_item.current_value
        // TODO: tính value_after = value_before + value_delta
        // TODO: lưu tracking_log
        // TODO: cập nhật kpi_item.current_value = value_after
        throw new UnsupportedOperationException("Chưa implement");
    }

    // Lấy lịch sử cập nhật của 1 tiêu chí
    public Object getHistory(Long kpiItemId) {
        // TODO: trả về list KpiTrackingLogDTO sắp xếp theo thời gian
        throw new UnsupportedOperationException("Chưa implement");
    }
}
