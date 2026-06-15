package vdt.kpimanagement.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "kpi_categories")
@Getter
@Setter
public class KpiCategory extends BaseEntity {

    @Column(name = "category_code", nullable = false, length = 50, unique = true)
    private String categoryCode;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
}
