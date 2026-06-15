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
public class PositionResponse {
    private Long id;
    private String positionCode;
    private String title;
    private Integer level;
    private Date createdAt;
    private Date updatedAt;
}
