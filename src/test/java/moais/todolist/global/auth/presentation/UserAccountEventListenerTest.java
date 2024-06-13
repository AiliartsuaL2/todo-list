package moais.todolist.global.auth.presentation;

import moais.todolist.global.auth.domain.UserAccount;
import moais.todolist.global.auth.persistence.UserAccountRepository;
import moais.todolist.global.domain.event.CreateMemberEvent;
import moais.todolist.global.domain.event.DeleteMemberEvent;
import moais.todolist.global.exception.ErrorMessage;
import moais.todolist.global.exception.EventException;
import moais.todolist.member.domain.RoleType;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@SpringBootTest
@Transactional
class UserAccountEventListenerTest {

    @Autowired
    ApplicationEventPublisher publisher;
    @Autowired
    UserAccountRepository userAccountRepository;

    @BeforeAll
    static void cleanUp(@Autowired JdbcTemplate jdbcTemplate) {
        jdbcTemplate.execute("truncate table user_account");
    }

    @Nested
    @DisplayName("회원 정보 생성 테스트")
    class CreateUserAccountTest {

        @Test
        @DisplayName("이벤트로 전달되는 객체 자체가 null인 경우 예외가 발생한다.")
        void test1() {
            // given
            CreateMemberEvent event = null;

            // when & then
            Assertions.assertThatThrownBy(() -> publisher.publishEvent(event))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("이벤트로 전달되는 객체의 회원ID 필드가 null인 경우 예외가 발생한다.")
        void test2() {
            // given
            String memberId = null;
            CreateMemberEvent event = new CreateMemberEvent(memberId);

            // when & then
            Assertions.assertThatThrownBy(() -> publisher.publishEvent(event))
                    .isInstanceOf(EventException.class)
                    .hasMessage(ErrorMessage.NOT_EXIST_MEMBER_ID.getMessage());
        }

        @Test
        @DisplayName("이벤트가 발행되면 회원 정보가 생성된다.")
        void test3() {
            // given
            String memberId = "memberId";

            // when
            publisher.publishEvent(new CreateMemberEvent(memberId));
            Optional<UserAccount> userAccountByMemberId = userAccountRepository.findUserAccountByMemberId(memberId);

            // then
            Assertions.assertThat(userAccountByMemberId).isPresent();
        }
    }

    @Nested
    @DisplayName("회원 탈퇴 테스트")
    class DeleteUserAccountTest {

        @Test
        @DisplayName("이벤트로 전달되는 객체 자체가 null인 경우 예외가 발생한다.")
        void test1() {
            // given
            DeleteMemberEvent event = null;

            // when & then
            Assertions.assertThatThrownBy(() -> publisher.publishEvent(event))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("이벤트로 전달되는 객체의 회원ID 필드가 null인 경우 예외가 발생한다.")
        void test2() {
            // given
            String memberId = null;

            // when & then
            Assertions.assertThatThrownBy(() -> publisher.publishEvent(new DeleteMemberEvent(memberId)))
                    .isInstanceOf(EventException.class)
                    .hasMessage(ErrorMessage.NOT_EXIST_MEMBER_ID.getMessage());
        }

        @Test
        @DisplayName("이벤트가 발행되면 회원 정보가 삭제된다.")
        void test3() {
            // given
            String memberId = "memberId";
            UserAccount userAccount = new UserAccount(memberId, RoleType.ROLE_USER.getAuthority());
            userAccountRepository.save(userAccount);

            // when
            publisher.publishEvent(new DeleteMemberEvent(memberId));
            Optional<UserAccount> userAccountByMemberId = userAccountRepository.findUserAccountByMemberId(memberId);

            // then
            Assertions.assertThat(userAccountByMemberId).isEmpty();
        }
    }
}