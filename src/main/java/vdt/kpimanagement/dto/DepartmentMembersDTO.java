package vdt.kpimanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO trả về danh sách thành viên của một phòng ban.
 * Dùng cho MANAGER xem chi tiết nhân sự phòng mình và các phòng con.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DepartmentMembersDTO {

    /** Thông tin phòng ban gốc được query */
    private DepartmentResponse department;

    /** Danh sách nhân viên trực tiếp thuộc phòng ban này */
    private List<EmployeeResponse> employees;

    /** Danh sách phòng ban con (sub-teams) trực thuộc phòng này */
    private List<SubDepartmentSummary> subDepartments;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SubDepartmentSummary {
        private Long id;
        private String departmentCode;
        private String name;
        private Long managerId;
        private String managerName;    // tên trưởng phòng con
        private int employeeCount;     // số nhân viên trong phòng con
        private List<EmployeeResponse> employees;
    }
}
