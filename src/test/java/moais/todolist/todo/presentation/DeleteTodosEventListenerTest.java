package moais.todolist.todo.presentation;

import moais.todolist.global.auth.domain.UserAccount;
import moais.todolist.global.auth.persistence.UserAccountRepository;
import moais.todolist.global.domain.event.DeleteMemberEvent;
import moais.todolist.global.exception.ErrorMessage;
import moais.todolist.global.exception.EventException;
import moais.todolist.member.domain.RoleType;
import moais.todolist.todo.domain.Todo;
import moais.todolist.todo.persistence.TodoRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;


@SpringBootTest
class DeleteTodosEventListenerTest {

    private static final String MEMBER_ID = "memberId";
    private static final String CONTENT = "content";

    @Autowired
    ApplicationEventPublisher publisher;

    @Autowired
    TodoRepository todoRepository;

    @Autowired
    UserAccountRepository userAccountRepository;

    @Autowired
    PlatformTransactionManager transactionManager;

    @BeforeAll
    static void init(@Autowired JdbcTemplate jdbcTemplate) {
        jdbcTemplate.execute("TRUNCATE TABLE TODO");
        jdbcTemplate.execute("TRUNCATE TABLE USER_ACCOUNT");
    }

    @AfterAll
    static void cleanUp(@Autowired JdbcTemplate jdbcTemplate) {
        jdbcTemplate.execute("TRUNCATE TABLE TODO");
        jdbcTemplate.execute("TRUNCATE TABLE USER_ACCOUNT");
    }

    @Nested
    @DisplayName("TODO 삭제 이벤트 리스너 테스트")
    class deleteTodo {

        TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);

        @Test
        @DisplayName("이벤트로 전달되는 객체 자체가 null인 경우 예외가 발생한다.")
        void test1() {
            // given
            DeleteMemberEvent event = null;

            // when & then
            Assertions.assertThatThrownBy(() -> {
                        transactionTemplate.executeWithoutResult(status -> {
                            publisher.publishEvent(event);
                        });
                    })
                    .isInstanceOf(IllegalArgumentException.class);

        }

        @Test
        @DisplayName("이벤트로 전달되는 객체의 회원ID 필드가 null인 경우 예외가 발생한다.")
        void test2() {
            // given
            String memberId = null;

            // when & then
            Assertions.assertThatThrownBy(() -> {
                        transactionTemplate.executeWithoutResult(status -> {
                            publisher.publishEvent(new DeleteMemberEvent(memberId));
                        });
                    })
                    .isInstanceOf(EventException.class)
                    .hasMessage(ErrorMessage.NOT_EXIST_MEMBER_ID.getMessage());

        }

        @Test
        @DisplayName("이벤트가 발행되면 회원의 TODO들이 삭제된다.")
        void test3() {
            // given
            UserAccount userAccount = new UserAccount(MEMBER_ID, RoleType.ROLE_USER.getAuthority());
            userAccountRepository.save(userAccount);
            DeleteMemberEvent event = new DeleteMemberEvent(MEMBER_ID);

            for (int i = 0; i < 10; i++) {
                Todo todo = new Todo(CONTENT, MEMBER_ID);
                todoRepository.save(todo);
            }

            // when
            transactionTemplate.executeWithoutResult(status -> {
                publisher.publishEvent(event);
            });

            // then
            Long count = todoRepository.findTodoCountByMemberId(MEMBER_ID);
            Assertions.assertThat(count).isEqualTo(0);
        }
    }
}
