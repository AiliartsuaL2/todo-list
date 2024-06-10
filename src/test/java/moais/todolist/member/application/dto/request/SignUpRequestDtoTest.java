package moais.todolist.member.application.dto.request;

import static org.assertj.core.api.Assertions.*;

import moais.todolist.member.domain.Member;
import moais.todolist.member.exception.ErrorMessage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class SignUpRequestDtoTest {

    private static final String NICKNAME = "nickname";
    private static final String LOGIN_ID = "loginId";
    private static final String PASSWORD = "password";

    @Test
    @DisplayName("필수 인자 - 로그인 ID 미존재시 예외가 발생한다.")
    void test1() {
        // given
        String loginId = null;
        String emptyLoginId = "";

        // when & then
        assertThatThrownBy(() -> new SignUpRequestDto(NICKNAME, loginId, PASSWORD))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ErrorMessage.NOT_EXIST_LOGIN_ID.getMessage());

        assertThatThrownBy(() -> new SignUpRequestDto(NICKNAME, emptyLoginId, PASSWORD))
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
        assertThatThrownBy(() -> new SignUpRequestDto(NICKNAME, LOGIN_ID, password))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ErrorMessage.NOT_EXIST_PASSWORD.getMessage());

        assertThatThrownBy(() -> new SignUpRequestDto(NICKNAME, LOGIN_ID, emptyPassword))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ErrorMessage.NOT_EXIST_PASSWORD.getMessage());
    }

    @Test
    @DisplayName("필수 인자 - 닉네임 미존재시 예외가 발생한다.")
    void test3() {
        // given
        String nickname = null;
        String emptyNickname = "";

        // when & then
        assertThatThrownBy(() -> new SignUpRequestDto(nickname, LOGIN_ID, PASSWORD))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ErrorMessage.NOT_EXIST_NICKNAME.getMessage());

        // when & then
        assertThatThrownBy(() -> new SignUpRequestDto(emptyNickname, LOGIN_ID, PASSWORD))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ErrorMessage.NOT_EXIST_NICKNAME.getMessage());
    }

    @Test
    @DisplayName("정상 요청시 입력 필드가 매핑된다.")
    void test4() {
        // given & when
        SignUpRequestDto requestDto = new SignUpRequestDto(NICKNAME, LOGIN_ID, PASSWORD);

        // then
        assertThat(requestDto.nickname()).isEqualTo(NICKNAME);
        assertThat(requestDto.loginId()).isEqualTo(LOGIN_ID);
        assertThat(requestDto.password()).isEqualTo(PASSWORD);
    }

    @Test
    @DisplayName("회원 엔티티 변환시 회원의 필드가 매핑된다.")
    void test5() {
        // given
        SignUpRequestDto requestDto = new SignUpRequestDto(NICKNAME, LOGIN_ID, PASSWORD);

        // when
        Member member = requestDto.toEntity();

        // then
        assertThat(member.getNickname()).isEqualTo(NICKNAME);
        assertThat(member.getLoginId()).isEqualTo(LOGIN_ID);
    }
}
