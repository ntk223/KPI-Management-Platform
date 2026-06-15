package vdt.kpimanagement.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "departments")
@Getter
@Setter
public class Department extends BaseEntity {

    @Column(name = "department_code", nullable = false, length = 50, unique = true)
    private String departmentCode;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    // Phòng ban cha (null = cấp cao nhất) — self-reference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Department parent;

    // Chỉ lưu ID của trưởng phòng để tránh circular reference với Employee
    @Column(name = "manager_id")
    private Long managerId;
}