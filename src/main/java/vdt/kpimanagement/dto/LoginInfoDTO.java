package vdt.kpimanagement.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

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
    String accessToken;
    String position;
}
