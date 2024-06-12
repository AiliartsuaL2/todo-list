package moais.todolist.global;


import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import moais.todolist.global.dto.ApiCommonResponse;
import moais.todolist.global.dto.ErrorMessage;
import moais.todolist.global.exception.EmptyTokenException;
import moais.todolist.global.exception.EventException;
import moais.todolist.global.exception.PermissionDeniedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    private static final String LOG_FORMAT = "Class : {}, StatusCode : {}, Message : {}";

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiCommonResponse<ErrorMessage>> illegalArgumentException(IllegalArgumentException ex){
        HttpStatus status = HttpStatus.BAD_REQUEST;
        return writeLogTraceAndResponse(status, ex);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ApiCommonResponse<ErrorMessage>> illegalStateException(IllegalStateException ex){
        HttpStatus status = HttpStatus.BAD_REQUEST;
        return writeLogTraceAndResponse(status, ex);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiCommonResponse<ErrorMessage>> httpMessageNotReadableException(HttpMessageNotReadableException ex){
        HttpStatus status = HttpStatus.BAD_REQUEST;
        return writeLogTraceAndResponse(status, ex);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiCommonResponse<ErrorMessage>> httpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex){
        HttpStatus status = HttpStatus.METHOD_NOT_ALLOWED;
        return writeLogTraceAndResponse(status, ex);
    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ApiCommonResponse<ErrorMessage>> jwtException(JwtException ex){
        HttpStatus status = HttpStatus.BAD_REQUEST;
        return writeLogTraceAndResponse(status, ex);
    }

    @ExceptionHandler(EmptyTokenException.class)
    public ResponseEntity<ApiCommonResponse<ErrorMessage>> emptyTokenException(EmptyTokenException ex){
        HttpStatus status = HttpStatus.BAD_REQUEST;
        return writeLogTraceAndResponse(status, ex);
    }

    @ExceptionHandler(PermissionDeniedException.class)
    public ResponseEntity<ApiCommonResponse<ErrorMessage>> permissionDeniedException(PermissionDeniedException ex){
        HttpStatus status = HttpStatus.FORBIDDEN;
        return writeLogTraceAndResponse(status, ex);
    }

    @ExceptionHandler(EventException.class)
    public ResponseEntity<ApiCommonResponse<ErrorMessage>> eventException(EventException ex){
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        return writeLogTraceAndResponse(status, ex);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiCommonResponse<ErrorMessage>> runtimeException(RuntimeException ex){
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        return writeLogTraceAndResponse(status, ex);
    }

    private ResponseEntity<ApiCommonResponse<ErrorMessage>> writeLogTraceAndResponse(HttpStatus httpStatus, Exception ex) {
        log.warn(
                LOG_FORMAT,
                ex.getClass().getSimpleName(),
                httpStatus.value(),
                ex.getMessage()
        );
        ErrorMessage errorMessage = new ErrorMessage(ex.getMessage());
        return ResponseEntity
                .status(httpStatus)
                .body(new ApiCommonResponse<>(false, errorMessage));
    }
}
