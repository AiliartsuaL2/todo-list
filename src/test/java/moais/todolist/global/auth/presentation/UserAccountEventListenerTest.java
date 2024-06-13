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
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Optional;
import org.springframework.transaction.support.TransactionTemplate;

@SpringBootTest
class UserAccountEventListenerTest {

    @Autowired
    ApplicationEventPublisher publisher;
    @Autowired
    UserAccountRepository userAccountRepository;
    @Autowired
    PlatformTransactionManager transactionManager;
    @Autowired
    UserAccountEventListener userAccountEventListener;
    
    @BeforeAll
    static void init(@Autowired JdbcTemplate jdbcTemplate) {
        jdbcTemplate.execute("TRUNCATE TABLE USER_ACCOUNT");
    }

    @AfterAll
    static void cleanUp(@Autowired JdbcTemplate jdbcTemplate) {
        jdbcTemplate.execute("TRUNCATE TABLE USER_ACCOUNT");
    }

    @Nested
    @DisplayName("회원 정보 생성 테스트")
    class CreateUserAccountTest {

        TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);

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
            Assertions.assertThatThrownBy(() -> {
                        transactionTemplate.executeWithoutResult(status -> {
                            publisher.publishEvent(event);
                        });
                    })
                    .isInstanceOf(EventException.class)
                    .hasMessage(ErrorMessage.NOT_EXIST_MEMBER_ID.getMessage());
        }

        @Test
        @DisplayName("이벤트가 발행되면 회원 정보가 생성된다.")
        void test3() {
            // given
            String memberId = "memberId";

            // when
            transactionTemplate.executeWithoutResult(status -> {
                publisher.publishEvent(new CreateMemberEvent(memberId));
            });

            // then
            Optional<UserAccount> userAccountByMemberId = userAccountRepository.findUserAccountByMemberId(memberId);
            Assertions.assertThat(userAccountByMemberId).isPresent();
        }
    }

    @Nested
    @DisplayName("회원 탈퇴 테스트")
    class DeleteUserAccountTest {

        TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);

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
            DeleteMemberEvent event = new DeleteMemberEvent(memberId);

            // when & then
            Assertions.assertThatThrownBy(() -> {
                        transactionTemplate.executeWithoutResult(status -> {
                            publisher.publishEvent(event);
                        });
                    })
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
            DeleteMemberEvent event = new DeleteMemberEvent(memberId);

            // when
            transactionTemplate.executeWithoutResult(status -> {
                publisher.publishEvent(event);
            });

            // then
            Optional<UserAccount> userAccountByMemberId = userAccountRepository.findUserAccountByMemberId(memberId);
            Assertions.assertThat(userAccountByMemberId).isEmpty();
        }
    }
}
