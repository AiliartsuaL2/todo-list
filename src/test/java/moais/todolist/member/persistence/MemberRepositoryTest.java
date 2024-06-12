package moais.todolist.member.persistence;

import moais.todolist.member.domain.Member;
import moais.todolist.testconfig.TestQueryDslConfig;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@Import(TestQueryDslConfig.class)
class MemberRepositoryTest {

    private static final String NICKNAME = "nickname";
    private static final String LOGIN_ID = "loginId";
    private static final String PASSWORD = "password123!";

    @Autowired
    MemberRepository memberRepository;

    @Nested
    @DisplayName("로그인 아이디로 회원 조회 테스트")
    class FindMemberByLoginIdAndDeleteYnIsFalse {

        @Test
        @DisplayName("생성한 회원과 로그인 아이디가 다르면, 조회 시 Optional.empty()를 반환한다.")
        void test1() {
            // given
            Member member = new Member(NICKNAME, LOGIN_ID, PASSWORD);
            memberRepository.save(member);
            String loginId = LOGIN_ID + "different";

            // when
            Optional<Member> result = memberRepository.findMemberByLoginIdAndDeleteYnIsFalse(loginId);

            // then
            Assertions.assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("생성한 회원이 탈퇴한 경우, 조회 시 Optional.empty()를 반환한다.")
        void test2() {
            // given
            Member member = new Member(NICKNAME, LOGIN_ID, PASSWORD);
            memberRepository.save(member);
            member.withdraw(member.getId(), LOGIN_ID, PASSWORD);

            // when
            Optional<Member> result = memberRepository.findMemberByLoginIdAndDeleteYnIsFalse(LOGIN_ID);

            // then
            Assertions.assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("정상 회원의 경우 생성한 필드가 모두 정상이다.")
        void test3() {
            // given
            Member member = new Member(NICKNAME, LOGIN_ID, PASSWORD);
            memberRepository.save(member);

            // when
            Optional<Member> result = memberRepository.findMemberByLoginIdAndDeleteYnIsFalse(LOGIN_ID);

            // then
            Assertions.assertThat(result).isPresent();
            Assertions.assertThat(result.get().getId()).isEqualTo(member.getId());
            Assertions.assertThat(result.get().getNickname()).isEqualTo(member.getNickname());
            Assertions.assertThat(result.get().getLoginId()).isEqualTo(member.getLoginId());
            Assertions.assertThat(result.get().getDeleteYn()).isFalse();
        }
    }
}