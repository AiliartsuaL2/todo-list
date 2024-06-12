package moais.todolist.global.auth.persistence;

import moais.todolist.global.auth.domain.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

// todo 테스트 코드 추가
public interface UserAccountRepository extends JpaRepository<UserAccount, String> {

    Optional<UserAccount> findUserAccountByMemberId(String memberId);

    void deleteUserAccountByMemberId(String memberId);
}
