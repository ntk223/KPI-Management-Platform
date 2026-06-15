package vdt.kpimanagement.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class LoginInfoDTO {
    String username;
    String fullName;
    String email;
    String position;
    List<String> roles;
    String accessToken;
    String refreshToken;
}
