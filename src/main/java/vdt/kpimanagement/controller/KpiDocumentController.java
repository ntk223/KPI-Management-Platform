package vdt.kpimanagement.controller;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import vdt.kpimanagement.constant.enums.DocumentTargetType;
import vdt.kpimanagement.dto.ApiResponse;
import vdt.kpimanagement.dto.KpiDocumentDetailDTO;
import vdt.kpimanagement.dto.KpiDocumentSaveDTO;
import vdt.kpimanagement.dto.KpiDocumentSearchDTO;
import vdt.kpimanagement.service.KpiDocumentService;

import java.util.List;

@RestController
@RequestMapping("/kpi-documents")
public class KpiDocumentController {

    private final KpiDocumentService kpiDocumentService;

    public KpiDocumentController(KpiDocumentService kpiDocumentService) {
        this.kpiDocumentService = kpiDocumentService;
    }

    // Lấy chi tiết phiếu KPI theo ID
    @GetMapping("/{id}")
    public ApiResponse<KpiDocumentDetailDTO> getById(@PathVariable Long id) {
        return ApiResponse.success(HttpStatus.OK.value(), "Chi tiết phiếu KPI",
                kpiDocumentService.getById(id));
    }

//    @GetMapping
//    public ApiResponse<KpiDocumentDetailDTO> getDocByType(@RequestParam String type) {
//        return ApiResponse.success(HttpStatus.OK.value(), "Chi tiết phiếu KPI",
//                kpiDocumentService.getDocByType(type));
//    }

    // Lấy KPI cá nhân của nhân viên đang đăng nhập
    @GetMapping("/my")
    public ApiResponse<Object> getMyDocument(@RequestParam Long cycleId,
                                              @RequestParam Long employeeId) {
        return ApiResponse.success(HttpStatus.OK.value(), "Phiếu KPI của tôi",
                kpiDocumentService.getMyDocument(cycleId, employeeId));
    }

    // Tạo phiếu KPI (DIRECTOR/MANAGER giao, hoặc EMPLOYEE đề xuất)
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<KpiDocumentDetailDTO> saveOrUpdate(@jakarta.validation.Valid @RequestBody KpiDocumentSaveDTO kpiDocumentSaveDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication != null && authentication.isAuthenticated())) {
            return ApiResponse.error(HttpStatus.UNAUTHORIZED.value(), "Không xác thực được người dùng");
        }
        String currentUser = authentication.getName();
        return ApiResponse.success(HttpStatus.CREATED.value(), "Tạo phiếu KPI thành công",
                kpiDocumentService.saveOrUpdate(kpiDocumentSaveDTO, currentUser));
    }

    @PostMapping("/search")
    public ApiResponse<List<KpiDocumentDetailDTO>> search(@RequestBody KpiDocumentSearchDTO searchDto) {
        return ApiResponse.success(HttpStatus.CREATED.value(), "Tìm kiếm phiếu KPI thành công",
                kpiDocumentService.search(searchDto));
    }

    // Gửi phiếu KPI đề xuất chờ duyệt
    @PatchMapping("/{id}/submit")
    public ApiResponse<Object> submit(@PathVariable Long id) {
        return ApiResponse.success(HttpStatus.OK.value(), "Gửi phiếu KPI thành công",
                kpiDocumentService.submit(id));
    }

    // Duyệt phiếu KPI (MANAGER)
    @PatchMapping("/{id}/approve")
    public ApiResponse<Object> approve(@PathVariable Long id, @RequestParam Long approverId) {
        return ApiResponse.success(HttpStatus.OK.value(), "Duyệt phiếu KPI thành công",
                kpiDocumentService.approve(id, approverId));
    }

    // Từ chối phiếu KPI (MANAGER)
    @PatchMapping("/{id}/reject")
    public ApiResponse<Object> reject(@PathVariable Long id, @RequestParam String reason) {
        return ApiResponse.success(HttpStatus.OK.value(), "Từ chối phiếu KPI thành công",
                kpiDocumentService.reject(id, reason));
    }

    // Đóng phiếu KPI cuối kỳ
    @PatchMapping("/{id}/close")
    public ApiResponse<Object> close(@PathVariable Long id) {
        return ApiResponse.success(HttpStatus.OK.value(), "Đóng phiếu KPI thành công",
                kpiDocumentService.close(id));
    }

    // Danh sách phiếu KPI chờ duyệt (MANAGER)
    @GetMapping("/pending-approvals")
    public ApiResponse<Object> getPendingApprovals(@RequestParam Long managerId) {
        return ApiResponse.success(HttpStatus.OK.value(), "Danh sách phiếu KPI chờ duyệt",
                kpiDocumentService.getPendingApprovals(managerId));
    }
}
