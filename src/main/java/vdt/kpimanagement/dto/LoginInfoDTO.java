package vdt.kpimanagement.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import vdt.kpimanagement.entity.Department;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class LoginInfoDTO {
    Long employeeId;
    String username;
    String fullName;
    String email;
    String position;
    List<String> roles;
    Department department;
    String accessToken;
    String refreshToken;
}
