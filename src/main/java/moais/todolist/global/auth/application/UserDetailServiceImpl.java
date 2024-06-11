package moais.todolist.global.auth.application;

import lombok.RequiredArgsConstructor;
import moais.todolist.global.auth.application.usecase.CreateUserAccountUseCase;
import moais.todolist.global.auth.domain.UserAccount;
import moais.todolist.global.auth.persistence.UserAccountRepository;
import moais.todolist.global.exception.ErrorMessage;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class UserDetailServiceImpl implements UserDetailsService, CreateUserAccountUseCase {

    private final UserAccountRepository userAccountRepository;

    @Override
    public UserDetails loadUserByUsername(String memberId) throws UsernameNotFoundException {
        return userAccountRepository.findUserAccountByMemberId(memberId)
                .orElseThrow(() -> new UsernameNotFoundException(ErrorMessage.NOT_EXIST_USER_ACCOUNT.getMessage()));
    }

    @Override
    @Transactional
    public void create(UserAccount userAccount) {
        userAccountRepository.save(userAccount);
    }
}
