package moais.todolist.global.auth.application;

import moais.todolist.global.auth.domain.UserAccount;
import moais.todolist.global.auth.persistence.UserAccountRepository;
import moais.todolist.global.exception.ErrorMessage;
import moais.todolist.member.domain.RoleType;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

class UserDetailServiceImplTest {

    UserAccountRepository userAccountRepository = mock(UserAccountRepository.class);

    UserDetailServiceImpl userDetailService = new UserDetailServiceImpl(userAccountRepository);

    @Nested
    @DisplayName("사용자 ID로 User Account 조회 테스트")
    class LoadUserByUsername{

        @Test
        @DisplayName("사용자 ID 미존재시 예외가 발생한다.")
        void test1() {
            // given
            String memberId = null;
            String emptyMemberId = "";

            // when & then
            Assertions.assertThatThrownBy(() -> userDetailService.loadUserByUsername(memberId))
                    .isInstanceOf(UsernameNotFoundException.class)
                    .hasMessage(ErrorMessage.NOT_EXIST_MEMBER_ID.getMessage());

            Assertions.assertThatThrownBy(() -> userDetailService.loadUserByUsername(emptyMemberId))
                    .isInstanceOf(UsernameNotFoundException.class)
                    .hasMessage(ErrorMessage.NOT_EXIST_MEMBER_ID.getMessage());
        }

        @Test
        @DisplayName("ID에 해당하는 회원 미존재시 예외가 발생한다.")
        void test2() {
            // given
            String memberId = "memberId";
            when(userAccountRepository.findUserAccountByMemberId(memberId))
                    .thenReturn(Optional.empty());

            // when & then
            Assertions.assertThatThrownBy(() -> userDetailService.loadUserByUsername(memberId))
                    .isInstanceOf(UsernameNotFoundException.class)
                    .hasMessage(ErrorMessage.NOT_EXIST_USER_ACCOUNT.getMessage());
        }

        @Test
        @DisplayName("정상 요청시 회원이 반환된다.")
        void test3() {
            // given
            String memberId = "memberId";
            UserAccount userAccount = mock(UserAccount.class);
            when(userAccountRepository.findUserAccountByMemberId(memberId))
                    .thenReturn(Optional.of(userAccount));

            // when
            UserDetails result = userDetailService.loadUserByUsername(memberId);

            // then
            Assertions.assertThat(result.getUsername()).isEqualTo(userAccount.getUsername());
        }
    }

    @Nested
    @DisplayName("생성 테스트")
    class Create {

        @Test
        @DisplayName("회원 정보 미존재시 예외가 발생한다.")
        void test1() {
            // given
            UserAccount userAccount = null;

            // when & then
            Assertions.assertThatThrownBy(() -> userDetailService.create(userAccount))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage(ErrorMessage.NOT_EXIST_USER_ACCOUNT.getMessage());
        }

        @Test
        @DisplayName("이미 존재하는 회원 정보의 경우 예외가 발생한다.")
        void test2() {
            // given
            String memberId = "memberId";
            UserAccount userAccount = new UserAccount(memberId, RoleType.ROLE_USER.getAuthority());
            when(userAccountRepository.findUserAccountByMemberId(memberId))
                    .thenReturn(Optional.of(userAccount));

            // when & then
            Assertions.assertThatThrownBy(() -> userDetailService.create(userAccount))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessage(ErrorMessage.ALREADY_EXIST_USER_ACCOUNT.getMessage());
        }

        @Test
        @DisplayName("정상 요청시 회원 정보가 생성된다.")
        void test3() {
            // given
            String memberId = "memberId";
            UserAccount userAccount = new UserAccount(memberId, RoleType.ROLE_USER.getAuthority());
            when(userAccountRepository.findUserAccountByMemberId(memberId))
                    .thenReturn(Optional.empty());

            // when
            userDetailService.create(userAccount);

            // then
            then(userAccountRepository)
                    .should(times(1))
                    .save(userAccount);
        }
    }

    @Nested
    @DisplayName("회원 정보 삭제 테스트")
    class Delete {

        @Test
        @DisplayName("사용자 ID 미존재시 예외가 발생한다.")
        void test1() {
            // given
            String memberId = null;
            String emptyMemberId = "";

            // when & then
            Assertions.assertThatThrownBy(() -> userDetailService.deleteByMemberId(memberId))
                    .isInstanceOf(UsernameNotFoundException.class)
                    .hasMessage(ErrorMessage.NOT_EXIST_MEMBER_ID.getMessage());

            Assertions.assertThatThrownBy(() -> userDetailService.deleteByMemberId(emptyMemberId))
                    .isInstanceOf(UsernameNotFoundException.class)
                    .hasMessage(ErrorMessage.NOT_EXIST_MEMBER_ID.getMessage());
        }

        @Test
        @DisplayName("ID에 해당하는 회원 미존재시 예외가 발생한다.")
        void test2() {
            // given
            String memberId = "memberId";
            when(userAccountRepository.findUserAccountByMemberId(memberId))
                    .thenReturn(Optional.empty());

            // when & then
            Assertions.assertThatThrownBy(() -> userDetailService.deleteByMemberId(memberId))
                    .isInstanceOf(UsernameNotFoundException.class)
                    .hasMessage(ErrorMessage.NOT_EXIST_USER_ACCOUNT.getMessage());
        }

        @Test
        @DisplayName("정상 요청시 회원 정보가 삭제된다.")
        void test3() {
            // given
            String memberId = "memberId";
            UserAccount userAccount = new UserAccount(memberId, RoleType.ROLE_USER.getAuthority());
            when(userAccountRepository.findUserAccountByMemberId(memberId))
                    .thenReturn(Optional.of(userAccount));

            // when
            userDetailService.deleteByMemberId(memberId);

            // then
            then(userAccountRepository)
                    .should(times(1))
                    .deleteUserAccountByMemberId(memberId);
        }
    }
}
