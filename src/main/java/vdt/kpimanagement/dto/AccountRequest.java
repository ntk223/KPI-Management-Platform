package vdt.kpimanagement.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vdt.kpimanagement.constant.enums.AccountStatus;
import vdt.kpimanagement.constant.enums.RoleCode;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountRequest {
    @NotBlank(message = "Tên đăng nhập không được để trống")
    @Size(max = 20, message = "Tên đăng nhập không được vượt quá 20 ký tự")
    private String username;

    @Size(min = 6, message = "Mật khẩu phải từ 6 ký tự trở lên")
    private String password;

    private AccountStatus status;
    private String provider;

    @NotNull(message = "Nhân viên liên kết không được để trống")
    private Long employeeId;

    private List<String> roles;
}
