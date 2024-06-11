package moais.todolist.global.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import moais.todolist.global.dto.ApiCommonResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtErrorResponseHandler {

    private final ObjectMapper objectMapper;

    public void generateJwtErrorResponse(HttpServletResponse response, String message, HttpStatus httpStatus) throws IOException {
        ApiCommonResponse<String> apiCommonResponse = new ApiCommonResponse<>(message, false);
        response.setStatus(httpStatus.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(apiCommonResponse));
    }
}
