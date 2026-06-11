package vdt.kpimanagement.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EmployeeDTO {
    String fullName;
    String email;
    String phoneNumber;
    Long departmentId;
    Long positionId;
}
