package vdt.kpimanagement.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vdt.kpimanagement.dto.KpiTrackingLogRequestDTO;
import vdt.kpimanagement.entity.Employee;
import vdt.kpimanagement.entity.KpiItem;
import vdt.kpimanagement.entity.KpiTrackingLog;
import vdt.kpimanagement.exception.ResourceNotFoundException;
import vdt.kpimanagement.repository.EmployeeRepo;
import vdt.kpimanagement.repository.KpiItemRepo;
import vdt.kpimanagement.repository.KpiTrackingLogRepo;

import java.math.BigDecimal;
import java.util.List;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

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
    @Transactional
    public KpiTrackingLog addProgress(KpiTrackingLogRequestDTO request) {
        KpiItem kpiItem = kpiItemRepo.findById(request.getKpiItemId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy tiêu chí KPI với ID: " + request.getKpiItemId()));

        Employee reporter = employeeRepo.findById(request.getReporterId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy nhân viên báo cáo với ID: " + request.getReporterId()));


        BigDecimal valueBefore = kpiItem.getCurrentValue() != null ? kpiItem.getCurrentValue() : BigDecimal.ZERO;
        BigDecimal valueAfter = request.getCurrentValue();
        BigDecimal valueDelta = valueAfter.subtract(valueBefore);

        // Cập nhật giá trị hiện tại của KPI Item
        kpiItem.setCurrentValue(valueAfter);
        kpiItemRepo.save(kpiItem);

        // Lưu log
        KpiTrackingLog log = new KpiTrackingLog();
        log.setKpiItem(kpiItem);
        log.setReporter(reporter);
        log.setValueBefore(valueBefore);
        log.setValueDelta(valueDelta);
        log.setValueAfter(valueAfter);
        log.setNotes(request.getNotes());

        return kpiTrackingLogRepo.save(log);
    }

    // Lấy lịch sử cập nhật của 1 tiêu chí
    public List<KpiTrackingLog> getHistory(Long kpiItemId) {
        return kpiTrackingLogRepo.findByKpiItem_IdAndIsDeletedFalseOrderByCreatedAtDesc(kpiItemId);
    }

    // Lấy nhật ký tiến độ gần đây (hỗ trợ lọc theo nhân viên hoặc phòng ban)
    public List<KpiTrackingLog> getRecentLogs(Long employeeId, Long departmentId, int limit) {
        Pageable pageable = PageRequest.of(0, limit, Sort.by("id").descending());
        if (employeeId != null) {
            return kpiTrackingLogRepo.findByReporter_IdAndIsDeletedFalseOrderByIdDesc(employeeId, pageable);
        } else if (departmentId != null) {
            return kpiTrackingLogRepo.findByReporter_Department_IdAndIsDeletedFalseOrderByIdDesc(departmentId, pageable);
        } else {
            return kpiTrackingLogRepo.findByIsDeletedFalseOrderByIdDesc(pageable);
        }
    }
}

