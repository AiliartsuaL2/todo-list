package moais.todolist.global.auth.persistence;

import static org.assertj.core.api.Assertions.*;

import java.util.Optional;
import moais.todolist.global.auth.domain.UserAccount;
import moais.todolist.member.domain.RoleType;
import moais.todolist.testconfig.TestQueryDslConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@Import(TestQueryDslConfig.class)
class UserAccountRepositoryTest {

    private static final String MEMBER_ID = "memberId";
    private static final String ROLE_TYPE = RoleType.ROLE_USER.getAuthority();

    @Autowired
    UserAccountRepository userAccountRepository;

    @Nested
    @DisplayName("사용자 ID로 회원 정보 조회 테스트")
    class FindUserAccountByMemberId {

        @Test
        @DisplayName("사용자 미존재시 Optional.empty()가 반환된다.")
        void test1() {
            // given & when
            Optional<UserAccount> userAccount = userAccountRepository.findUserAccountByMemberId(MEMBER_ID);

            // then
            assertThat(userAccount)
                    .isEmpty();
        }

        @Test
        @DisplayName("정상 상태에서 조회시 조회 결과가 isPresent 이다.")
        void test2() {
            // given
            UserAccount userAccount = new UserAccount(MEMBER_ID, ROLE_TYPE);
            userAccountRepository.save(userAccount);

            // when
            Optional<UserAccount> result = userAccountRepository.findUserAccountByMemberId(MEMBER_ID);

            // then
            assertThat(result).isPresent();
        }
    }

    @Nested
    @DisplayName("사용자 ID로 회원 정보 삭제 테스트")
    class DeleteUserAccountByMemberId {

        @Test
        @DisplayName("사용자 삭제시, 조회 할 경우 조회 결과가 empty 이다")
        void test1() {
            // given
            UserAccount userAccount = new UserAccount(MEMBER_ID, ROLE_TYPE);
            userAccountRepository.save(userAccount);

            // when
            userAccountRepository.deleteUserAccountByMemberId(MEMBER_ID);

            // then
            Optional<UserAccount> result = userAccountRepository.findUserAccountByMemberId(MEMBER_ID);
            assertThat(result).isEmpty();
        }
    }
}
