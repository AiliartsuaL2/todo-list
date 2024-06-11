package moais.todolist.global.config;

import moais.todolist.global.auth.application.JwtService;
import moais.todolist.global.auth.application.provider.JwtProvider;
import moais.todolist.global.auth.domain.UserAccount;
import moais.todolist.global.auth.presentation.AuthenticationTestController;
import moais.todolist.global.exception.ErrorMessage;
import moais.todolist.global.handler.JwtAccessDeniedHandler;
import moais.todolist.global.handler.JwtAuthenticationEntryPoint;
import moais.todolist.global.handler.JwtErrorResponseHandler;
import moais.todolist.global.handler.JwtExceptionFilter;

import moais.todolist.member.domain.RoleType;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.h2.H2ConsoleAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles(value = "test")
@WebMvcTest(
        value = AuthenticationTestController.class,
        includeFilters = @ComponentScan.Filter(classes = {EnableWebSecurity.class})
)
@Import({
        JwtExceptionFilter.class,
        JwtAccessDeniedHandler.class,
        JwtAuthenticationEntryPoint.class,
        JwtErrorResponseHandler.class,
        H2ConsoleAutoConfiguration.class
})
class SecurityConfigTest {

    private static final String NOT_USE_AUTHENTICATION_URL = "/api/v1/auth/not-use/test";
    private static final String USE_AUTHENTICATION_URL = "/api/v1/auth/use/test";

    @Autowired
    private MockMvc mockMvc;
    @Value("${jwt.secret-key}")
    private String secretKey;

    @MockBean
    UserDetailsService userDetailsService;

    @SpyBean
    JwtProvider jwtProvider;

    @SpyBean
    JwtService jwtService;

    @Nested
    @DisplayName("인증 필요 없는 요청 테스트")
    class NotUseAuthentication {
        String url = NOT_USE_AUTHENTICATION_URL;

        @Test
        @DisplayName("토큰 없이 요청시 200이 응답된다.")
        void test1() throws Exception {
            mockMvc.perform(get(url))
                    .andExpect(status().isOk());
        }
    }

    @Nested
    @DisplayName("인증 필요 요청 테스트")
    class UseAuthentication {
        String url = USE_AUTHENTICATION_URL;

        @Test
        @DisplayName("토큰 없이 요청시 401이 응답된다.")
        void test1() throws Exception {
            // given & when
            String responseBody = mockMvc.perform(get(url))
                    .andExpect(status().isUnauthorized())
                    .andReturn()
                    .getResponse()
                    .getContentAsString();

            // then
            Assertions.assertThat(responseBody)
                    .contains(ErrorMessage.NOT_EXIST_TOKEN.getMessage());
        }

        @Test
        @DisplayName("토큰이 만료된 경우 401이 응답된다.")
        void test2() throws Exception {
            // given
            String payLoad = "memberId";
            JwtProvider jwtProvider = new JwtProvider(secretKey, 0, 0);
            String accessToken = jwtProvider.createAccessToken(payLoad);

            // when
            String responseBody = mockMvc.perform(get(url)
                            .header("Authorization", "Bearer " + accessToken))
                    .andExpect(status().isUnauthorized())
                    .andReturn()
                    .getResponse()
                    .getContentAsString();

            // then
            Assertions.assertThat(responseBody)
                    .contains(ErrorMessage.EXPIRED_TOKEN.getMessage());
        }

        @Test
        @DisplayName("토큰 형식이 올바르지 않은 경우 401이 응답된다.")
        void test3() throws Exception {
            // given
            String accessToken = "invalidToken";

            // when
            String responseBody = mockMvc.perform(get(url)
                            .header("Authorization", "Bearer " + accessToken))
                    .andExpect(status().isUnauthorized())
                    .andReturn()
                    .getResponse()
                    .getContentAsString();

            // then
            Assertions.assertThat(responseBody)
                    .contains(ErrorMessage.MALFORMED_TOKEN.getMessage());
        }

        @Test
        @DisplayName("토큰 시그니처가 올바르지 않은 경우 401이 응답된다.")
        void test4() throws Exception {
            // given
            String payLoad = "memberId";
            String secretKey = "invalidSecretKey";
            JwtProvider jwtProvider = new JwtProvider(secretKey, 10000, 10000);
            String accessToken = jwtProvider.createAccessToken(payLoad);

            // when
            String responseBody = mockMvc.perform(get(url)
                            .header("Authorization", "Bearer " + accessToken))
                    .andExpect(status().isUnauthorized())
                    .andReturn()
                    .getResponse()
                    .getContentAsString();

            // then
            Assertions.assertThat(responseBody)
                    .contains(ErrorMessage.SIGNATURE_TOKEN_EXCEPTION.getMessage());
        }

        @Test
        @DisplayName("토큰 Payload가 서버에 등록되지 않은 경우 403이 응답된다.")
        void test5() throws Exception {
            // given
            String payLoad = "memberId";
            String accessToken = jwtProvider.createAccessToken(payLoad);
            when(userDetailsService.loadUserByUsername(any()))
                    .thenThrow(UsernameNotFoundException.class);

            // when
            String responseBody = mockMvc.perform(get(url)
                            .header("Authorization", "Bearer " + accessToken))
                    .andExpect(status().isForbidden())
                    .andReturn()
                    .getResponse()
                    .getContentAsString();

            // then
            Assertions.assertThat(responseBody)
                    .contains(ErrorMessage.INCORRECT_TOKEN_TYPE.getMessage());
        }

        @Test
        @DisplayName("정상 요청시 200이 응답된다.")
        void test6() throws Exception {
            // given
            String payLoad = "memberId";
            JwtProvider jwtProvider = new JwtProvider(secretKey, 10000, 10000);
            String accessToken = jwtProvider.createAccessToken(payLoad);

            UserAccount userAccount = new UserAccount(payLoad, RoleType.ROLE_USER.getAuthority());
            when(userDetailsService.loadUserByUsername(any()))
                    .thenReturn(userAccount);

            // when & then
            mockMvc.perform(get(url)
                            .header("Authorization", "Bearer " + accessToken))
                    .andExpect(status().isOk());
        }
    }
}