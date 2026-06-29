package vdt.kpimanagement.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class KpiCategoryRequest {
    @NotBlank(message = "Mã danh mục không được để trống")
    @Size(max = 20, message = "Mã danh mục không được vượt quá 20 ký tự")
    private String categoryCode;

    @NotBlank(message = "Tên danh mục không được để trống")
    @Size(max = 100, message = "Tên danh mục không được vượt quá 100 ký tự")
    private String name;

    private String description;
}
