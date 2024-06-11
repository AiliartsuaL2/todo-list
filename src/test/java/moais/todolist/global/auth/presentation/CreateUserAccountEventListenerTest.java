package moais.todolist.global.auth.presentation;

import moais.todolist.global.auth.domain.UserAccount;
import moais.todolist.global.auth.persistence.UserAccountRepository;
import moais.todolist.global.auth.presentation.dto.request.CreateUserAccountEvent;
import moais.todolist.global.exception.ErrorMessage;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;

import java.util.Optional;

@SpringBootTest
class CreateUserAccountEventListenerTest {

    @Autowired
    ApplicationEventPublisher publisher;
    @Autowired
    UserAccountRepository loadUserAccountPort;

    @Nested
    @DisplayName("회원 정보 생성 테스트")
    class CreateUserAccountTest {

        @Test
        @DisplayName("이벤트로 전달되는 객체 자체가 null인 경우 예외가 발생한다.")
        void test1() {
            // given
            CreateUserAccountEvent event = null;

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
            Assertions.assertThatThrownBy(() -> publisher.publishEvent(new CreateUserAccountEvent(memberId)))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage(ErrorMessage.NOT_EXIST_MEMBER_ID.getMessage());
        }

        @Test
        @DisplayName("이벤트가 발행되면 회원 정보가 생성된다.")
        void test3() {
            // given
            String memberId = "memberId";

            // when
            publisher.publishEvent(new CreateUserAccountEvent(memberId));
            Optional<UserAccount> userAccountByMemberId = loadUserAccountPort.findUserAccountByMemberId(memberId);

            // then
            Assertions.assertThat(userAccountByMemberId).isPresent();
        }
    }

}