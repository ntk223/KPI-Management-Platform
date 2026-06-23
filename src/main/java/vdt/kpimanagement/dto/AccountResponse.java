package vdt.kpimanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vdt.kpimanagement.constant.enums.AccountStatus;

import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountResponse {
    private Long id;
    private String username;
    private AccountStatus status;
    private String provider;

    // Thông tin nhân viên liên kết
    private Long employeeId;
    private String employeeCode;
    private String fullName;
    private String email;

    // Danh sách vai trò
    private List<String> roles;

    private Date createdAt;
    private Date updatedAt;
}
