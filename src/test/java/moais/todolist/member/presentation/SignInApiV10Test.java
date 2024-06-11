package moais.todolist.member.presentation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import moais.todolist.global.auth.domain.Token;
import moais.todolist.global.handler.JwtAccessDeniedHandler;
import moais.todolist.global.handler.JwtAuthenticationEntryPoint;
import moais.todolist.global.handler.JwtExceptionFilter;
import moais.todolist.member.application.dto.response.SignInResponseDto;
import moais.todolist.member.application.usecase.SignInUseCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(
        controllers = SignInApiV10.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = JwtExceptionFilter.class),
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = JwtAccessDeniedHandler.class),
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = JwtAuthenticationEntryPoint.class)}
)
class SignInApiV10Test {
    private static final String PATH = "/api/v10/members/sign-in";

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    SignInUseCase signInUseCase;

    @Test
    @DisplayName("Body 미존재시 400이 응답된다.")
    void test1() throws Exception {
        // given & when & then
        mockMvc.perform(post(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("정상 요청시, Status는 200, Body에는 accessToken, refreshToken, memberId가 반한된다.")
    void test2() throws Exception {
        // given
        String accessToken = "accessToken";
        String refreshToken = "refreshToken";
        String memberId = "memberId";
        SignInResponseDto responseDto = new SignInResponseDto(memberId, new Token(accessToken, refreshToken));
        when(signInUseCase.signIn(any()))
                .thenReturn(responseDto);
        String requestJson ="{"
                + "\"loginId\": \"loginId\","
                + "\"password\": \"password\""
                + "}";

        // when & then
        String response = mockMvc.perform(post(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertThat(response).contains(memberId);
        assertThat(response).contains(accessToken);
        assertThat(response).contains(refreshToken);
    }
}
