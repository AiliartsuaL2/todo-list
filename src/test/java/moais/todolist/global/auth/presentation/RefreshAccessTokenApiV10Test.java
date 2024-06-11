package moais.todolist.global.auth.presentation;

import com.google.gson.Gson;
import moais.todolist.global.auth.application.usecase.RenewAccessTokenUseCase;
import moais.todolist.global.auth.presentation.dto.response.RefreshAccessTokenResponseDto;
import moais.todolist.global.dto.ApiCommonResponse;
import moais.todolist.global.handler.JwtAccessDeniedHandler;
import moais.todolist.global.handler.JwtAuthenticationEntryPoint;
import moais.todolist.global.handler.JwtExceptionFilter;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(
        controllers = RefreshAccessTokenApiV10.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = JwtExceptionFilter.class),
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = JwtAccessDeniedHandler.class),
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = JwtAuthenticationEntryPoint.class)}
)
class RefreshAccessTokenApiV10Test {
    private static final String PATH = "/api/v1.0/token/refresh";

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    RenewAccessTokenUseCase renewAccessTokenUseCase;

    @Test
    @DisplayName("RefreshToken 미존재시 400이 응답된다.")
    void test1() throws Exception {
        // given & when & then
        mockMvc.perform(post(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("정상 요청시, Status 200, access Token이 발급된다.")
    void test2() throws Exception {
        // given
        String requestRefreshToken = "Bearer refreshToken";
        String accessToken = "accessToken";

        RefreshAccessTokenResponseDto tokenResponseDto = new RefreshAccessTokenResponseDto(accessToken);
        ApiCommonResponse<RefreshAccessTokenResponseDto> responseDto = new ApiCommonResponse<>(tokenResponseDto, true);
        String responseJson = new Gson().toJson(responseDto);

        when(renewAccessTokenUseCase.renewAccessToken(requestRefreshToken))
                .thenReturn(accessToken);

        // when
        String response = mockMvc.perform(post(PATH)
                        .header("Authorization", requestRefreshToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse().getContentAsString();

        // then
        assertThat(response)
                .isEqualTo(responseJson);
    }
}