package mahfuz.mia.SecureNotesAPIs.exception;

import lombok.extern.slf4j.Slf4j;
import mahfuz.mia.SecureNotesAPIs.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandling {
    @ExceptionHandler(value = RuntimeException.class)
    public ResponseEntity<ApiResponse<?>> handleException(RuntimeException ex){
        log.error("RuntimeException occurred: {}", ex.getMessage());
        ApiResponse<?> response = new ApiResponse<>(
                HttpStatus.BAD_REQUEST.toString(),
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage(),
                true,
                null
        );
        return ResponseEntity.badRequest().body(response);
    }
}
