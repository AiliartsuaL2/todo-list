package moais.todolist.global.auth.persistence;

import moais.todolist.global.auth.domain.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserAccountRepository extends JpaRepository<UserAccount, String> {

    Optional<UserAccount> findUserAccountByMemberId(String memberId);

    void deleteUserAccountByMemberId(String memberId);
}
