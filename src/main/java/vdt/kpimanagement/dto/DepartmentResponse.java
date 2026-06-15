package vdt.kpimanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DepartmentResponse {
    private Long id;
    private String departmentCode;
    private String name;
    private Long parentId;    // ID phòng ban cha
    private String parentName; // Tên phòng ban cha (để hiển thị)
    private Long managerId;
    private Date createdAt;
    private Date updatedAt;
}
