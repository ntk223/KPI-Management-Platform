package vdt.kpimanagement.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DepartmentRequest {
    @NotBlank(message = "Mã phòng ban không được để trống")
    @Size(max = 20, message = "Mã phòng ban không được vượt quá 20 ký tự")
    private String departmentCode;

    @NotBlank(message = "Tên phòng ban không được để trống")
    @Size(max = 100, message = "Tên phòng ban không được vượt quá 100 ký tự")
    private String name;

    private Long parentId;   // null nếu là phòng ban cấp cao nhất
    private Long managerId;  // ID nhân viên là trưởng phòng
}
