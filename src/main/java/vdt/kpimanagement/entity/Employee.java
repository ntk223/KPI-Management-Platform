package vdt.kpimanagement.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "employees")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Employee extends BaseEntity {
    @Column(name = "employee_code", nullable = false, length = 50)
    private String employeeCode;
    @Column(name = "email", nullable = false, length = 100)
    private String email;
    @Column(name = "full_name", nullable = false, length = 100)
    private String fullName;
    @Column(name = "phone_number", length = 10)
    private String phoneNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private Department department;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "position_id")
    private Position position;
}
