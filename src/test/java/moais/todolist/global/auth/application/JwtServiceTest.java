package moais.todolist.global.auth.application;

import moais.todolist.global.auth.application.provider.CreateTokenProvider;
import moais.todolist.global.auth.application.provider.GetTokenInfoProvider;
import moais.todolist.global.auth.domain.Token;
import moais.todolist.global.exception.EmptyTokenException;
import moais.todolist.global.exception.ErrorMessage;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


class JwtServiceTest {
    CreateTokenProvider createTokenProvider = mock(CreateTokenProvider.class);
    GetTokenInfoProvider getTokenInfoProvider = mock(GetTokenInfoProvider.class);
    UserDetailsService userDetailsService = mock(UserDetailsService.class);
    JwtService jwtService = new JwtService(createTokenProvider, getTokenInfoProvider, userDetailsService);

    @Nested
    @DisplayName("토큰 생성 테스트")
    class Create {
        @Test
        @DisplayName("회원 Id 미존재시 예외가 발생한다.")
        void test1() {
            // given
            String memberId = null;

            // when & then
            Assertions.assertThatThrownBy(() -> jwtService.create(memberId))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage(ErrorMessage.NOT_EXIST_MEMBER_ID.getMessage());
        }

        @Test
        @DisplayName("가입되지 않은 회원 (userAccount가 없는경우)인 경우 예외가 발생한다.")
        void test2() {
            // given
            String memberId = "memberId";
            when(userDetailsService.loadUserByUsername(memberId))
                    .thenThrow(new UsernameNotFoundException(ErrorMessage.NOT_EXIST_MEMBER.getMessage()));

            // when & then
            Assertions.assertThatThrownBy(() -> jwtService.create(memberId))
                    .isInstanceOf(UsernameNotFoundException.class)
                    .hasMessage(ErrorMessage.NOT_EXIST_MEMBER.getMessage());
        }

        @Test
        @DisplayName("정상 토큰 생성 요청시 토큰이 발급된다.")
        void test3() {
            // given
            String memberId = "memberId";

            String resultToken = "resultToken";
            when(createTokenProvider.createAccessToken(any()))
                    .thenReturn(resultToken);
            when(createTokenProvider.createRefreshToken(any()))
                    .thenReturn(resultToken);

            // when
            Token token = jwtService.create(memberId);

            // then
            Assertions.assertThat(token.accessToken())
                    .isNotBlank();
            Assertions.assertThat(token.refreshToken())
                    .isNotBlank();
        }
    }

    @Nested
    @DisplayName("payload 추출 테스트")
    class ExtractPayload {
        @Test
        @DisplayName("토큰 미존재시 예외가 발생한다.")
        void test1() {
            // given
            String accessToken = null;

            // when & then
            Assertions.assertThatThrownBy(() -> jwtService.extractPayload(accessToken))
                    .isInstanceOf(EmptyTokenException.class)
                    .hasMessage(ErrorMessage.NOT_EXIST_TOKEN.getMessage());
        }

        @Test
        @DisplayName("정상 추출시 payload가 응답된다.")
        void test2() {
            // given
            String accessToken = "token";
            String payload = "payload";
            when(getTokenInfoProvider.getPayload(accessToken))
                    .thenReturn(payload);

            // when
            String result = jwtService.extractPayload(accessToken);

            // then
            Assertions.assertThat(result).isEqualTo(payload);
        }
    }

    @Nested
    @DisplayName("액세스 토큰 재생성 테스트")
    class RenewAccessToken {
        @Test
        @DisplayName("토큰 미존재시 예외가 발생한다.")
        void test1() {
            // given
            String refreshToken = null;

            // when & then
            Assertions.assertThatThrownBy(() -> jwtService.renewAccessToken(refreshToken))
                    .isInstanceOf(EmptyTokenException.class)
                    .hasMessage(ErrorMessage.NOT_EXIST_TOKEN.getMessage());
        }

        @Test
        @DisplayName("정상 요청시 액세스 토큰이 반환된다.")
        void test2() {
            // given
            String refreshToken = "refreshToken";
            String accessToken = "accessToken";
            String payload = "payload";
            when(getTokenInfoProvider.getPayload(refreshToken))
                    .thenReturn(payload);
            when(createTokenProvider.createAccessToken(payload))
                    .thenReturn(accessToken);

            // when
            String result = jwtService.renewAccessToken(refreshToken);

            // then
            Assertions.assertThat(result).isEqualTo(accessToken);
        }
    }

    @Nested
    @DisplayName("토큰 검증 테스트")
    class IsValid {
        @Test
        @DisplayName("토큰 미존재시 예외가 발생한다.")
        void test1() {
            // given
            String accessToken = null;

            // when & then
            Assertions.assertThatThrownBy(() -> jwtService.isValid(accessToken))
                    .isInstanceOf(EmptyTokenException.class)
                    .hasMessage(ErrorMessage.NOT_EXIST_TOKEN.getMessage());
        }

        @Test
        @DisplayName("토큰 유효성 검증 실패시 false를 반환한다.")
        void test2() {
            // given
            String accessToken = "token";
            when(getTokenInfoProvider.isValid(accessToken))
                    .thenReturn(false);

            // when
            boolean result = jwtService.isValid(accessToken);

            // then
            Assertions.assertThat(result).isFalse();
        }

        @Test
        @DisplayName("토큰 유효성 검증 성공시 true를 반환한다.")
        void test3() {
            // given
            String accessToken = "token";
            when(getTokenInfoProvider.isValid(accessToken))
                    .thenReturn(true);

            // when
            boolean result = jwtService.isValid(accessToken);

            // then
            Assertions.assertThat(result).isTrue();
        }
    }

    @Nested
    @DisplayName("Authentication 발급 테스트")
    class GetAuthentication {
        @Test
        @DisplayName("토큰 미존재시 예외가 발생한다.")
        void test1() {
            // given
            String accessToken = null;

            // when & then
            Assertions.assertThatThrownBy(() -> jwtService.getAuthentication(accessToken))
                    .isInstanceOf(EmptyTokenException.class)
                    .hasMessage(ErrorMessage.NOT_EXIST_TOKEN.getMessage());
        }

        @Test
        @DisplayName("정상 요청시 Authentication이 발급된다.")
        void test2() {
            // given
            String accessToken = "accessToken";
            String payload = "payload";
            UserDetails userDetails = mock(UserDetails.class);

            when(getTokenInfoProvider.getPayload(accessToken))
                    .thenReturn(payload);
            when(userDetailsService.loadUserByUsername(payload))
                    .thenReturn(userDetails);

            // when
            Authentication authentication = jwtService.getAuthentication(accessToken);

            // then
            Assertions.assertThat(authentication.isAuthenticated()).isTrue();
        }
    }
}