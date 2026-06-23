package vdt.kpimanagement.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import vdt.kpimanagement.constant.enums.CycleStatus;
import vdt.kpimanagement.constant.enums.CycleType;

import java.time.LocalDate;

@Entity
@Table(name = "kpi_cycles")
@Getter
@Setter
public class KpiCycle extends BaseEntity {

    @Column(name = "cycle_code", nullable = false, length = 50, unique = true)
    private String cycleCode;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 20)
    private CycleType type;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private CycleStatus status = CycleStatus.PLANNING;

    @Column(name = "created_by", nullable = false, length = 20)
    private String createdBy;
}