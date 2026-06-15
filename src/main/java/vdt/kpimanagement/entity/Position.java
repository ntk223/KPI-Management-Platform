package vdt.kpimanagement.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "positions")
@Getter
@Setter
public class Position extends BaseEntity {

    @Column(name = "position_code", nullable = false, length = 50, unique = true)
    private String positionCode;

    @Column(name = "title", nullable = false, length = 100)
    private String title;

    // 1=Nhân viên, 2=Trưởng nhóm, 3=Trưởng phòng, 4=Phó GĐ, 5=Giám đốc
    @Column(name = "level", nullable = false)
    private Integer level;
}