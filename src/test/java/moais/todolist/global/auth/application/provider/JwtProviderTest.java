package moais.todolist.global.auth.application.provider;

import io.jsonwebtoken.JwtException;
import moais.todolist.global.exception.ErrorMessage;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;


class JwtProviderTest {

    private static final String SECRET_KEY = "secretKey";
    private static final Long ACCESS_TOKEN_VALID_TIME = 3600L;
    private static final Long REFRESH_TOKEN_VALID_TIME = 2592000L;

    JwtProvider jwtProvider = new JwtProvider(SECRET_KEY, ACCESS_TOKEN_VALID_TIME, REFRESH_TOKEN_VALID_TIME);

    @Nested
    @DisplayName("액세스 토큰 생성 테스트")
    class CreateAccessToken {
        @Test
        @DisplayName("payload 미존재시 예외가 발생한다.")
        void test1() {
            // given
            String payload = null;

            // when & then
            Assertions.assertThatThrownBy(() -> jwtProvider.createAccessToken(payload))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage(ErrorMessage.NOT_EXIST_PAYLOAD.getMessage());
        }

        @Test
        @DisplayName("토큰 정상 생성시 header, payload, signature로 구분된다.")
        void test2() {
            // given
            String payload = "payload";

            // when
            String accessToken = jwtProvider.createAccessToken(payload);

            // then
            Assertions.assertThat(accessToken.split("\\.")).hasSize(3);
        }
    }

    @Nested
    @DisplayName("리프레시 토큰 생성 테스트")
    class CreateRefreshToken {
        @Test
        @DisplayName("payload 미존재시 예외가 발생한다.")
        void test1() {
            // given
            String payload = null;

            // when & then
            Assertions.assertThatThrownBy(() -> jwtProvider.createRefreshToken(payload))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage(ErrorMessage.NOT_EXIST_PAYLOAD.getMessage());
        }

        @Test
        @DisplayName("토큰 정상 생성시 header, payload, signature로 구분된다.")
        void test2() {
            // given
            String payload = "payload";

            // when
            String accessToken = jwtProvider.createRefreshToken(payload);

            // then
            Assertions.assertThat(accessToken.split("\\.")).hasSize(3);
        }
    }

    @Nested
    @DisplayName("페이로드 조회 테스트")
    class GetPayload {
        @Test
        @DisplayName("토큰 미존재시 예외가 발생한다.")
        void test1() {
            // given
            String token = null;

            // when & then
            Assertions.assertThatThrownBy(() -> jwtProvider.getPayload(token))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage(ErrorMessage.NOT_EXIST_TOKEN.getMessage());
        }

        @Test
        @DisplayName("만료된 토큰 추출시 예외가 발생한다.")
        void test2() {
            // given
            JwtProvider jwtProvider = new JwtProvider(SECRET_KEY, 0L, 0L);
            String payload = "payload";
            String accessToken = jwtProvider.createAccessToken(payload);

            // when & then
            Assertions.assertThatThrownBy(() -> jwtProvider.getPayload(accessToken))
                    .isInstanceOf(JwtException.class)
                    .hasMessage(ErrorMessage.EXPIRED_TOKEN.getMessage());
        }

        @Test
        @DisplayName("정상 토큰 조회시 payload가 반환된다.")
        void test3() {
            // given
            String payload = "payload";
            String accessToken = jwtProvider.createAccessToken(payload);

            // when
            String result = jwtProvider.getPayload(accessToken);

            // then
            Assertions.assertThat(result).isEqualTo(payload);
        }
    }

    @Nested
    @DisplayName("토큰 검증 테스트")
    class isValid {
        @Test
        @DisplayName("정상적인 토큰 형태가 아닌 경우, 예외가 발생한다.")
        void test1() {
            // given
            String accessToken = "invalidToken";

            // when & then
            Assertions.assertThatThrownBy(() -> jwtProvider.isValid(accessToken))
                    .isInstanceOf(JwtException.class)
                    .hasMessage(ErrorMessage.MALFORMED_TOKEN.getMessage());

        }

        @Test
        @DisplayName("토큰이 만료된 경우, 예외가 발생한다.")
        void test2() {
            // given
            String payload = "payload";
            JwtProvider jwtProvider = new JwtProvider(SECRET_KEY, 0L, 0L);
            String accessToken = jwtProvider.createAccessToken(payload);

            // when & then
            Assertions.assertThatThrownBy(() -> jwtProvider.isValid(accessToken))
                    .isInstanceOf(JwtException.class)
                    .hasMessage(ErrorMessage.EXPIRED_TOKEN.getMessage());
        }

        @Test
        @DisplayName("정상적인 토큰인 경우, true가 반환된다.")
        void test3() {
            // given
            String payload = "payload";
            String accessToken = jwtProvider.createAccessToken(payload);

            // when
            boolean result = jwtProvider.isValid(accessToken);

            // then
            Assertions.assertThat(result).isTrue();
        }
    }
}