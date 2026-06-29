package vdt.kpimanagement.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import vdt.kpimanagement.constant.enums.DocumentTargetType;
import vdt.kpimanagement.constant.enums.SourceType;

import java.util.List;

@Getter
@Setter
public class KpiDocumentSaveDTO {

    // Bằng null nếu TẠO MỚI, có giá trị nếu CẬP NHẬT
    private Long id;

    @NotNull(message = "ID chu kỳ KPI là bắt buộc")
    @Min(value = 1, message = "ID chu kỳ KPI phải là số nguyên dương")
    private Long cycleId;

    @NotNull(message = "Loại đối tượng nhận KPI không được để trống")
    private DocumentTargetType targetType;

    // Sẽ validate logic trong Service dựa theo targetType (COMPANY thì được null, DEPT/EMP thì bắt buộc)
    private Long targetId;

    private Long parentDocId; // Nullable - Nếu thuộc cấu trúc cascade (cha-con)

    private SourceType sourceType = SourceType.ASSIGNED; // Mặc định là ASSIGNED nếu không truyền

    @Valid
    private List<KpiItemDTO> kpiItems;
}
