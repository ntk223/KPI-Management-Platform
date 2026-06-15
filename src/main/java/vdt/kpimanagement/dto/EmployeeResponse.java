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
public class EmployeeResponse {
    private Long id;
    private String employeeCode;
    private String fullName;
    private String email;
    private String phoneNumber;
    private Long departmentId;
    private String departmentName;
    private Long positionId;
    private String positionTitle;
    private Date createdAt;
    private Date updatedAt;
}
