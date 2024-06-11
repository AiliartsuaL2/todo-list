package moais.todolist.member.presentation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.StandardCharsets;
import moais.todolist.global.auth.application.JwtService;
import moais.todolist.global.auth.application.provider.JwtProvider;
import moais.todolist.global.auth.domain.UserAccount;
import moais.todolist.global.handler.JwtAccessDeniedHandler;
import moais.todolist.global.handler.JwtAuthenticationEntryPoint;
import moais.todolist.global.handler.JwtErrorResponseHandler;
import moais.todolist.global.handler.JwtExceptionFilter;
import moais.todolist.member.application.usecase.WithdrawUseCase;
import moais.todolist.member.domain.RoleType;
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
        value = WithdrawApiV10.class,
        includeFilters = @ComponentScan.Filter(classes = {EnableWebSecurity.class})
)
@Import({
        JwtExceptionFilter.class,
        JwtAccessDeniedHandler.class,
        JwtAuthenticationEntryPoint.class,
        JwtErrorResponseHandler.class,
        H2ConsoleAutoConfiguration.class
})
class WithdrawApiV10Test {

    private static final String REQUEST_JSON ="{"
            + "\"loginId\": \"loginId\","
            + "\"password\": \"password\""
            + "}";
    private static String ACCESS_TOKEN;
    private static final String PATH = "/api/v10/members";
    private static final String SUCCESS_MESSAGE = "회원 탈퇴가 정상적으로 처리되었습니다.";

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    JwtService jwtService;
    @MockBean
    WithdrawUseCase withdrawUseCase;

    @BeforeAll
    static void init(@Value("${jwt.secret-key}") String secretKey) {
        ACCESS_TOKEN = "Bearer " +
                new JwtProvider(secretKey, 30, 1)
                        .createAccessToken("memberId");
    }

    @Test
    @DisplayName("Body 미존재시 400이 응답된다.")
    void test1() throws Exception {
        // given
        String accessToken = ACCESS_TOKEN;

        // when & then
        mockMvc.perform(delete(PATH)
                        .header("Authorization", accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("토큰 미존재시 401이 응답된다.")
    void test2() throws Exception {
        // given
        String body = REQUEST_JSON;

        // when & then
        mockMvc.perform(delete(PATH)
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("정상 요청시, Status는 200, Body에는 성공 메세지가 응답 된다.")
    void test3() throws Exception {
        // given
        UserAccount userAccount = new UserAccount("memberId", RoleType.ROLE_USER.getAuthority());
        when(jwtService.isValid(any()))
                .thenReturn(true);
        when(jwtService.getAuthentication(any()))
                .thenReturn(new UsernamePasswordAuthenticationToken(userAccount, "", userAccount.getAuthorities()));

        String accessToken = ACCESS_TOKEN;
        String body = REQUEST_JSON;

        // when & then
        String response = mockMvc.perform(delete(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", accessToken)
                        .content(body)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        assertThat(response).contains(SUCCESS_MESSAGE);
    }
}
