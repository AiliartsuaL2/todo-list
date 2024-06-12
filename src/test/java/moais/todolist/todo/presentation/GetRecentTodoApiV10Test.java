package moais.todolist.todo.presentation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.charset.StandardCharsets;
import moais.todolist.global.auth.application.JwtService;
import moais.todolist.global.auth.application.provider.JwtProvider;
import moais.todolist.global.auth.domain.UserAccount;
import moais.todolist.global.dto.ApiCommonResponse;
import moais.todolist.global.handler.JwtAccessDeniedHandler;
import moais.todolist.global.handler.JwtAuthenticationEntryPoint;
import moais.todolist.global.handler.JwtErrorResponseHandler;
import moais.todolist.global.handler.JwtExceptionFilter;
import moais.todolist.member.domain.RoleType;
import moais.todolist.todo.application.dto.response.GetTodoResponseDto;
import moais.todolist.todo.application.usecase.GetRecentTodoUseCase;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.h2.H2ConsoleAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;


@ActiveProfiles(value = "test")
@WebMvcTest(
        value = GetRecentTodoApiV10.class,
        includeFilters = @ComponentScan.Filter(classes = {EnableWebSecurity.class})
)
@Import({
        JwtExceptionFilter.class,
        JwtAccessDeniedHandler.class,
        JwtAuthenticationEntryPoint.class,
        JwtErrorResponseHandler.class,
        H2ConsoleAutoConfiguration.class
})
class GetRecentTodoApiV10Test {

    private static String ACCESS_TOKEN;
    private static final String PATH = "/api/v10/todos/recent";
    private static final GetTodoResponseDto RESPONSE_DTO = new GetTodoResponseDto(
            "todoId", "content", "status", "createdAt", "updatedAt");

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    JwtService jwtService;
    @MockBean
    GetRecentTodoUseCase getRecentTodoUseCase;
    @Autowired
    ObjectMapper objectMapper;

    @BeforeAll
    static void init(@Value("${jwt.secret-key}") String secretKey) {
        ACCESS_TOKEN = "Bearer " +
                new JwtProvider(secretKey, 30, 1)
                        .createAccessToken("memberId");
    }

    @Test
    @DisplayName("토큰 미존재시 401이 응답된다.")
    void test1() throws Exception {
        // given & when & then
        mockMvc.perform(get(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("정상 요청시, Status는 200, Body에는 응답 객체가 응답된다.")
    void test2() throws Exception {
        // given
        UserAccount userAccount = new UserAccount("memberId", RoleType.ROLE_USER.getAuthority());
        when(jwtService.isValid(any()))
                .thenReturn(true);
        when(jwtService.getAuthentication(any()))
                .thenReturn(new UsernamePasswordAuthenticationToken(userAccount, "", userAccount.getAuthorities()));
        when(getRecentTodoUseCase.getRecentTodo(any()))
                .thenReturn(RESPONSE_DTO);
        ApiCommonResponse<GetTodoResponseDto> apiResponse = new ApiCommonResponse<>(true,
                RESPONSE_DTO);

        String responseJson = objectMapper.writeValueAsString(apiResponse);
        String accessToken = ACCESS_TOKEN;

        // when & then
        String response = mockMvc.perform(get(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", accessToken)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        assertThat(response).isEqualTo(responseJson);
        then(getRecentTodoUseCase)
                .should(times(1))
                .getRecentTodo(any());
    }
}

