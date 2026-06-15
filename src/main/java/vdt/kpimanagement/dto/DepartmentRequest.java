package vdt.kpimanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DepartmentRequest {
    private String departmentCode;
    private String name;
    private Long parentId;   // null nếu là phòng ban cấp cao nhất
    private Long managerId;  // ID nhân viên là trưởng phòng
}
