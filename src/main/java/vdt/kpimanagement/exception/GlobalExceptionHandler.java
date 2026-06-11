package vdt.kpimanagement.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import vdt.kpimanagement.dto.ApiResponse;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Ví dụ: Bất kỳ chỗ nào throw IllegalArgumentException, API sẽ trả về lỗi 400 chuẩn
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<ErrorResponse> handleBadRequest(IllegalArgumentException ex) {
        ErrorResponse error = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage(),
                LocalDateTime.now()
        );
        return ApiResponse.error(error.getStatus(), error.getMessage());
    }

    // Bạn có thể thêm các hàm hứng lỗi quá tải dữ liệu, lỗi 500 hệ thống tại đây...
}
