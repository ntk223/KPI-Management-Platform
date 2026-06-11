package vdt.kpimanagement.dto;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL) // Nếu trường nào bằng null (ví dụ khi lỗi không có data), Jackson sẽ tự ẩn cột đó đi để JSON sạch hơn
public class ApiResponse<T> {
    private boolean success;
    private int status;
    private String message;
    private T data;
    private LocalDateTime timestamp;

    // Hàm tiện ích nhanh khi API thành công và có trả về dữ liệu
    public static <T> ApiResponse<T> success(int status, String message, T data) {
        return ApiResponse.<T>builder()
                .success(true)
                .status(status)
                .message(message)
                .data(data)
                .timestamp(LocalDateTime.now())
                .build();
    }

    // Hàm tiện ích nhanh khi API thành công nhưng không cần trả dữ liệu (ví dụ xóa mềm thành công)
    public static <T> ApiResponse<T> success(int status, String message) {
        return ApiResponse.<T>builder()
                .success(true)
                .status(status)
                .message(message)
                .timestamp(LocalDateTime.now())
                .build();
    }

    // Hàm tiện ích nhanh dùng khi API gặp lỗi
    public static <T> ApiResponse<T> error(int status, String message) {
        return ApiResponse.<T>builder()
                .success(false)
                .status(status)
                .message(message)
                .timestamp(LocalDateTime.now())
                .build();
    }
}