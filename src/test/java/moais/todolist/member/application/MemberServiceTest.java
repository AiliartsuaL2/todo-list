package moais.todolist.member.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.mockito.BDDMockito.then;

import java.util.Optional;
import java.util.UUID;
import moais.todolist.global.auth.application.usecase.CreateTokenUseCase;
import moais.todolist.global.auth.domain.Token;
import moais.todolist.member.application.dto.request.SignInRequestDto;
import moais.todolist.member.application.dto.request.SignUpRequestDto;
import moais.todolist.member.application.dto.request.WithdrawRequestDto;
import moais.todolist.member.application.dto.response.SignInResponseDto;
import moais.todolist.member.domain.Member;
import moais.todolist.member.exception.ErrorMessage;
import moais.todolist.member.persistence.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class MemberServiceTest {

    private static final String MEMBER_ID = UUID.randomUUID().toString();
    private static final String NICKNAME = "nickname";
    private static final String LOGIN_ID = "loginId";
    private static final String PASSWORD = "password";
    private static final String ACCESS_TOKEN = "accessToken";
    private static final String REFRESH_TOKEN = "refreshToken";
    private static final Token TOKEN = new Token(ACCESS_TOKEN, REFRESH_TOKEN);

    private final MemberRepository memberRepository = mock(MemberRepository.class);
    private final CreateTokenUseCase createTokenUseCase = mock(CreateTokenUseCase.class);
    private final MemberService memberService = new MemberService(memberRepository, createTokenUseCase);

    @Nested
    @DisplayName("회원 가입 테스트")
    class SignUp {

        @Test
        @DisplayName("로그인 ID의 회원이 이미 존재하는 경우 예외가 발생한다.")
        void test1() {
            // given
            SignUpRequestDto requestDto = new SignUpRequestDto(NICKNAME, LOGIN_ID, PASSWORD);
            when(memberRepository.findMemberByLoginId(requestDto.loginId()))
                    .thenReturn(Optional.of(mock(Member.class)));

            // when & then
            Assertions.assertThatThrownBy(() -> memberService.signUp(requestDto))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessage(ErrorMessage.ALREADY_EXIST_LOGIN_ID.getMessage());
        }

        @Test
        @DisplayName("정상 상태의 요청인 경우 회원이 저장된다.")
        void test2() {
            // given
            SignUpRequestDto requestDto = new SignUpRequestDto(NICKNAME, LOGIN_ID, PASSWORD);
            when(memberRepository.findMemberByLoginId(requestDto.loginId()))
                    .thenReturn(Optional.empty());

            // when
            memberService.signUp(requestDto);

            // then
            then(memberRepository)
                    .should(times(1))
                    .save(any(Member.class));
        }
    }

    @Nested
    @DisplayName("로그인 테스트")
    class SignIn {

        @Test
        @DisplayName("로그인 ID에 해당하는 회원이 존재하지 않는 경우 예외가 발생한다.")
        void test1() {
            // given
            SignInRequestDto requestDto = new SignInRequestDto(LOGIN_ID, PASSWORD);
            when(memberRepository.findMemberByLoginId(requestDto.loginId()))
                    .thenReturn(Optional.empty());

            // when & then
            Assertions.assertThatThrownBy(() -> memberService.signIn(requestDto))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage(ErrorMessage.NOT_EXIST_MEMBER.getMessage());
        }

        @Test
        @DisplayName("정상 요청시 응답 DTO에 회원 ID, 토큰이 매핑된다.")
        void test2() {
            // given
            SignInRequestDto requestDto = new SignInRequestDto(LOGIN_ID, PASSWORD);
            Member member = mock(Member.class);
            when(member.signIn(requestDto.password()))
                    .thenReturn(MEMBER_ID);
            when(createTokenUseCase.create(MEMBER_ID))
                    .thenReturn(TOKEN);
            when(memberRepository.findMemberByLoginId(requestDto.loginId()))
                    .thenReturn(Optional.of(member));

            // when
            SignInResponseDto responseDto = memberService.signIn(requestDto);

            // then
            assertThat(responseDto.memberId()).isEqualTo(MEMBER_ID);
            assertThat(responseDto.token().accessToken()).isEqualTo(ACCESS_TOKEN);
            assertThat(responseDto.token().refreshToken()).isEqualTo(REFRESH_TOKEN);
        }
    }

    @Nested
    @DisplayName("회원 탈퇴 테스트")
    class Withdraw {
        @Test
        @DisplayName("회원 ID가 존재하지 않는 경우 예외가 발생한다.")
        void test1() {
            // given
            WithdrawRequestDto requestDto = new WithdrawRequestDto(MEMBER_ID, LOGIN_ID, PASSWORD);
            String memberId = null;

            // when & then
            assertThatThrownBy(() -> memberService.withdraw(requestDto, memberId))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage(ErrorMessage.NOT_EXIST_PAYLOAD.getMessage());
        }

        @Test
        @DisplayName("로그인 ID에 해당하는 회원이 존재하지 않는 경우 예외가 발생한다.")
        void test2() {
            // given
            WithdrawRequestDto requestDto = new WithdrawRequestDto(MEMBER_ID, LOGIN_ID, PASSWORD);
            when(memberRepository.findMemberByLoginId(requestDto.loginId()))
                    .thenReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> memberService.withdraw(requestDto, MEMBER_ID))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage(ErrorMessage.NOT_EXIST_MEMBER.getMessage());
        }

        @Test
        @DisplayName("토큰의 ID와 탈퇴하고자하는 회원의 ID가 다른 경우 예외가 발생한다.")
        void test3() {
            // given
            WithdrawRequestDto requestDto = new WithdrawRequestDto(MEMBER_ID, LOGIN_ID, PASSWORD);
            Member member = spy(Member.class);
            when(member.getId())
                    .thenReturn(MEMBER_ID);
            when(memberRepository.findMemberByLoginId(requestDto.loginId()))
                    .thenReturn(Optional.of(member));
            String payload = MEMBER_ID + "payload";

            // when & then
            assertThatThrownBy(() -> memberService.withdraw(requestDto, payload))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage(ErrorMessage.NOT_MATCHED_ID.getMessage());
        }

        @Test
        @DisplayName("정상 요청시 withdraw 메서드가 실행된다.")
        void test4() {
            // given
            WithdrawRequestDto requestDto = new WithdrawRequestDto(MEMBER_ID, LOGIN_ID, PASSWORD);
            Member member = mock(Member.class);
            when(member.getId())
                    .thenReturn(MEMBER_ID);
            when(memberRepository.findMemberByLoginId(requestDto.loginId()))
                    .thenReturn(Optional.of(member));

            // when
            memberService.withdraw(requestDto, MEMBER_ID);

            // then
            then(member)
                    .should(times(1))
                    .withdraw(requestDto.memberId(), requestDto.loginId(), requestDto.password());
        }
    }
}
