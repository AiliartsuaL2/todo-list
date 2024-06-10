package moais.todolist.member.application.dto.request;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.UUID;
import moais.todolist.member.exception.ErrorMessage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class WithdrawRequestDtoTest {

    private static final String MEMBER_ID = UUID.randomUUID().toString();
    private static final String LOGIN_ID = "loginId";
    private static final String PASSWORD = "password";

    @Test
    @DisplayName("필수 인자 - 회원 ID 미존재시 예외가 발생한다.")
    void test1() {
        // given
        String memberId = null;
        String emptyMemberId = "";

        // when & then
        assertThatThrownBy(() -> new WithdrawRequestDto(memberId, LOGIN_ID, PASSWORD))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ErrorMessage.NOT_EXIST_MEMBER_ID.getMessage());

        assertThatThrownBy(() -> new WithdrawRequestDto(emptyMemberId, LOGIN_ID, PASSWORD))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ErrorMessage.NOT_EXIST_MEMBER_ID.getMessage());
    }

    @Test
    @DisplayName("필수 인자 - 로그인 ID 미존재시 예외가 발생한다.")
    void test2() {
        // given
        String loginId = null;
        String emptyLoginId = "";

        // when & then
        assertThatThrownBy(() -> new WithdrawRequestDto(MEMBER_ID, loginId, PASSWORD))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ErrorMessage.NOT_EXIST_LOGIN_ID.getMessage());

        assertThatThrownBy(() -> new WithdrawRequestDto(MEMBER_ID, emptyLoginId, PASSWORD))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ErrorMessage.NOT_EXIST_LOGIN_ID.getMessage());
    }

    @Test
    @DisplayName("필수 인자 - 비밀번호 미존재시 예외가 발생한다.")
    void test3() {
        // given
        String password = null;
        String emptyPassword = "";

        // when & then
        assertThatThrownBy(() -> new WithdrawRequestDto(MEMBER_ID, LOGIN_ID, password))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ErrorMessage.NOT_EXIST_PASSWORD.getMessage());

        assertThatThrownBy(() -> new WithdrawRequestDto(MEMBER_ID, LOGIN_ID, emptyPassword))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ErrorMessage.NOT_EXIST_PASSWORD.getMessage());
    }

    @Test
    @DisplayName("정상 요청시 입력 필드가 매핑된다.")
    void test4() {
        // given & when
        WithdrawRequestDto requestDto = new WithdrawRequestDto(MEMBER_ID, LOGIN_ID, PASSWORD);

        // then
        assertThat(requestDto.memberId()).isEqualTo(MEMBER_ID);
        assertThat(requestDto.loginId()).isEqualTo(LOGIN_ID);
        assertThat(requestDto.password()).isEqualTo(PASSWORD);
    }
}
