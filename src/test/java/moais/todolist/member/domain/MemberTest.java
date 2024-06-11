package moais.todolist.member.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.util.UUID;
import moais.todolist.member.exception.ErrorMessage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class MemberTest {

    private static final String MEMBER_ID = UUID.randomUUID().toString();
    private static final String NICKNAME = "nickname";
    private static final String LOGIN_ID = "loginId";
    private static final String PASSWORD = "password";
    private static final Member MEMBER = new Member(NICKNAME, LOGIN_ID, PASSWORD);

    @Nested
    @DisplayName("회원 가입 테스트")
    class SignUp {

        @Test
        @DisplayName("필수 인자 - 로그인 ID가 존재하지 않는 경우 예외가 발생한다")
        void test1() {
            // given
            String loginId = null;
            String emptyLoginId = "";


            // when & then
            assertThatThrownBy(() -> new Member(NICKNAME, loginId, PASSWORD))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage(ErrorMessage.NOT_EXIST_LOGIN_ID.getMessage());

            assertThatThrownBy(() -> new Member(NICKNAME, emptyLoginId, PASSWORD))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage(ErrorMessage.NOT_EXIST_LOGIN_ID.getMessage());
        }

        @Test
        @DisplayName("필수 인자 - 닉네임이 존재하지 않는 경우 예외가 발생한다")
        void test2() {
            // given
            String nickname = null;
            String emptyNickname = "";

            // when & then
            assertThatThrownBy(() -> new Member(nickname, LOGIN_ID, PASSWORD))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage(ErrorMessage.NOT_EXIST_NICKNAME.getMessage());

            assertThatThrownBy(() -> new Member(emptyNickname, LOGIN_ID, PASSWORD))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage(ErrorMessage.NOT_EXIST_NICKNAME.getMessage());
        }

        @Test
        @DisplayName("필수 인자 - 비밀번호가 존재하지 않는 경우 예외가 발생한다")
        void test3() {
            // given
            String password = null;
            String emptyPassword = "";

            // when & then
            assertThatThrownBy(() -> new Member(NICKNAME, LOGIN_ID, password))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage(ErrorMessage.NOT_EXIST_PASSWORD.getMessage());

            assertThatThrownBy(() -> new Member(NICKNAME, LOGIN_ID, emptyPassword))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage(ErrorMessage.NOT_EXIST_PASSWORD.getMessage());
        }

        @Test
        @DisplayName("로그인 ID 입력이 최대 길이(30자)가 넘어가는 경우 예외가 발생한다.")
        void test4() {
            // given
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < 50; i++) {
                stringBuilder.append(".");
            }
            String loginId = stringBuilder.toString();

            // when & then
            assertThatThrownBy(() -> new Member(NICKNAME, loginId, PASSWORD))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage(ErrorMessage.OVER_THAN_MAX_SIZE_AT_LOGIN_ID.getMessage());
        }

        @Test
        @DisplayName("닉네임의 입력이 최대 길이(30자)가 넘어가는 경우 예외가 발생한다.")
        void test5() {
            // given
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < 50; i++) {
                stringBuilder.append(".");
            }
            String nickname = stringBuilder.toString();

            // when & then
            assertThatThrownBy(() -> new Member(nickname, LOGIN_ID, PASSWORD))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage(ErrorMessage.OVER_THAN_MAX_SIZE_AT_NICKNAME.getMessage());
        }

        @Test
        @DisplayName("정상 생성시 입력 필드가 매핑되고, 삭제여부 컬럼이 False가 된다.")
        void test6() {
            // given & when
            Member member = new Member(NICKNAME, LOGIN_ID, PASSWORD);

            // then
            assertThat(member.getNickname())
                    .isEqualTo(NICKNAME);
            assertThat(member.getLoginId())
                    .isEqualTo(LOGIN_ID);
            assertThat(member.isMatchedPassword(PASSWORD))
                    .isTrue();
            assertThat(member.getDeleteYn())
                    .isEqualTo(Boolean.FALSE);
        }
    }

    @Nested
    @DisplayName("로그인 테스트")
    class SignIn {

        @Test
        @DisplayName("필수 인자 - 비밀번호가 존재하지 않는 경우 예외가 발생한다.")
        void test1() {
            // given
            String password = null;
            String emptyPassword = "";

            // when & then
            assertThatThrownBy(() -> MEMBER.signIn(password))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage(ErrorMessage.NOT_EXIST_PASSWORD.getMessage());

            assertThatThrownBy(() -> MEMBER.signIn(emptyPassword))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage(ErrorMessage.NOT_EXIST_PASSWORD.getMessage());
        }

        @Test
        @DisplayName("입력 비밀번호가 회원의 비밀번호와 일치하지 않는 경우 예외가 발생한다.")
        void test2() {
            // given
            String differentPassword = PASSWORD + "different";

            // when & then
            assertThatThrownBy(() -> MEMBER.signIn(differentPassword))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage(ErrorMessage.NOT_MATCHED_PASSWORD.getMessage());
        }

        @Test
        @DisplayName("로그인 성공시 회원 ID를 반환한다.")
        void test3() {
            // given
            Member member = spy(MEMBER);
            when(member.getId())
                    .thenReturn(MEMBER_ID);

            // when
            String result = member.signIn(PASSWORD);

            // then
            assertThat(result).isEqualTo(MEMBER_ID);
        }
    }

    @Nested
    @DisplayName("회원 탈퇴 테스트")
    class Withdraw {
        @Test
        @DisplayName("필수 인자 - 회원 ID가 존재하지 않는 경우 예외가 발생한다.")
        void test1() {
            // given
            String memberId = null;
            String emptyMemberId = "";

            // when & then
            assertThatThrownBy(() -> MEMBER.withdraw(memberId, LOGIN_ID, PASSWORD))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage(ErrorMessage.NOT_EXIST_MEMBER_ID.getMessage());

            assertThatThrownBy(() -> MEMBER.withdraw(emptyMemberId, LOGIN_ID, PASSWORD))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage(ErrorMessage.NOT_EXIST_MEMBER_ID.getMessage());
        }

        @Test
        @DisplayName("필수 인자 - 로그인 ID가 존재하지 않는 경우 예외가 발생한다.")
        void test2() {
            // given
            String loginId = null;
            String emptyLoginId = "";

            // when & then
            assertThatThrownBy(() -> MEMBER.withdraw(MEMBER_ID, loginId, PASSWORD))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage(ErrorMessage.NOT_EXIST_LOGIN_ID.getMessage());

            assertThatThrownBy(() -> MEMBER.withdraw(MEMBER_ID, emptyLoginId, PASSWORD))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage(ErrorMessage.NOT_EXIST_LOGIN_ID.getMessage());
        }

        @Test
        @DisplayName("필수 인자 - 비밀번호가 존재하지 않는 경우 예외가 발생한다.")
        void test3() {
            // given
            String password = null;
            String emptyPassword = "";

            // when & then
            assertThatThrownBy(() -> MEMBER.withdraw(MEMBER_ID, LOGIN_ID, password))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage(ErrorMessage.NOT_EXIST_PASSWORD.getMessage());

            assertThatThrownBy(() -> MEMBER.withdraw(MEMBER_ID, LOGIN_ID, emptyPassword))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage(ErrorMessage.NOT_EXIST_PASSWORD.getMessage());
        }

        @Test
        @DisplayName("payload와 회원 ID가 일치하지 않는 경우 예외가 발생한다.")
        void test4() {
            // given
            Member member = spy(MEMBER);
            when(member.getId())
                    .thenReturn(MEMBER_ID+"different");

            // when & then
            assertThatThrownBy(() -> member.withdraw(MEMBER_ID, LOGIN_ID, PASSWORD))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage(ErrorMessage.NOT_MATCHED_ID.getMessage());
        }

        @Test
        @DisplayName("입력 로그인 ID가 회원의 로그인 ID와 일치하지 않는 경우 예외가 발생한다.")
        void test5() {
            // given
            String loginId = LOGIN_ID + "different";

            // when & then
            assertThatThrownBy(() -> MEMBER.withdraw(MEMBER_ID, loginId, PASSWORD))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage(ErrorMessage.NOT_MATCHED_LOGIN_ID.getMessage());
        }

        @Test
        @DisplayName("입력 비밀번호가 회원의 비밀번호와 일치하지 않는 경우 예외가 발생한다.")
        void test6() {
            // given
            String password = PASSWORD + "different";

            // when & then
            assertThatThrownBy(() -> MEMBER.withdraw(MEMBER_ID, LOGIN_ID, password))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage(ErrorMessage.NOT_MATCHED_PASSWORD.getMessage());
        }

        @Test
        @DisplayName("회원 탈퇴 성공시 삭제 컬럼은 True, 삭제 일시가 생성된다.")
        void test7() {
            // given
            Member member = spy(MEMBER);
            when(member.getId())
                    .thenReturn(MEMBER_ID);

            // when
            member.withdraw(MEMBER_ID, LOGIN_ID, PASSWORD);

            // then
            assertThat(member.getDeleteYn()).isTrue();
            assertThat(member.getDeletedAt()).isNotNull();
        }
    }
}
