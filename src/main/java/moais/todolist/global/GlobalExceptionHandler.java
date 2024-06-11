package moais.todolist.global;


import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import moais.todolist.global.dto.ApiCommonResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    private static final String LOG_FORMAT = "Class : {}, StatusCode : {}, Message : {}";

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiCommonResponse<String>> illegalArgumentException(IllegalArgumentException ex){
        HttpStatus status = HttpStatus.BAD_REQUEST;
        return writeLogTraceAndResponse(status, ex);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ApiCommonResponse<String>> illegalStateException(IllegalStateException ex){
        HttpStatus status = HttpStatus.BAD_REQUEST;
        return writeLogTraceAndResponse(status, ex);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiCommonResponse<String>> httpMessageNotReadableException(HttpMessageNotReadableException ex){
        HttpStatus status = HttpStatus.BAD_REQUEST;
        return writeLogTraceAndResponse(status, ex);
    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ApiCommonResponse<String>> jwtException(JwtException ex){
        HttpStatus status = HttpStatus.BAD_REQUEST;
        return writeLogTraceAndResponse(status, ex);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiCommonResponse<String>> runtimeException(RuntimeException ex){
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        return writeLogTraceAndResponse(status, ex);
    }

    private ResponseEntity<ApiCommonResponse<String>> writeLogTraceAndResponse(HttpStatus httpStatus, Exception ex) {
        log.warn(
                LOG_FORMAT,
                ex.getClass().getSimpleName(),
                httpStatus.value(),
                ex.getMessage()
        );
        return ResponseEntity
                .status(httpStatus)
                .body(new ApiCommonResponse<>(ex.getMessage(), false));
    }
}
