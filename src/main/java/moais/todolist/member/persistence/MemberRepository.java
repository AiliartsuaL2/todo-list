package moais.todolist.member.persistence;

import java.util.Optional;
import moais.todolist.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, String> {
    Optional<Member> findMemberByLoginIdAndDeleteYnIsFalse(String loginId);
}
