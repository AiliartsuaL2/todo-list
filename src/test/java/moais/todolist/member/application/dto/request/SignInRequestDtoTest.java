package moais.todolist.member.application.dto.request;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import moais.todolist.member.exception.ErrorMessage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class SignInRequestDtoTest {

    private static final String LOGIN_ID = "loginId";
    private static final String PASSWORD = "password";

    @Test
    @DisplayName("필수 인자 - 로그인 ID 미존재시 예외가 발생한다.")
    void test1() {
        // given
        String loginId = null;
        String emptyLoginId = "";

        // when & then
        assertThatThrownBy(() -> new SignInRequestDto(loginId, PASSWORD))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ErrorMessage.NOT_EXIST_LOGIN_ID.getMessage());

        assertThatThrownBy(() -> new SignInRequestDto(emptyLoginId, PASSWORD))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ErrorMessage.NOT_EXIST_LOGIN_ID.getMessage());
    }

    @Test
    @DisplayName("필수 인자 - 비밀번호 미존재시 예외가 발생한다.")
    void test2() {
        // given
        String password = null;
        String emptyPassword = "";

        // when & then
        assertThatThrownBy(() -> new SignInRequestDto(LOGIN_ID, password))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ErrorMessage.NOT_EXIST_PASSWORD.getMessage());

        assertThatThrownBy(() -> new SignInRequestDto(LOGIN_ID, emptyPassword))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ErrorMessage.NOT_EXIST_PASSWORD.getMessage());
    }

    @Test
    @DisplayName("필수 인자 - 비밀번호 미존재시 예외가 발생한다.")
    void test3() {
        // given & when
        SignInRequestDto requestDto = new SignInRequestDto(LOGIN_ID, PASSWORD);

        // then
        assertThat(requestDto.loginId()).isEqualTo(LOGIN_ID);
        assertThat(requestDto.password()).isEqualTo(PASSWORD);
    }
}
