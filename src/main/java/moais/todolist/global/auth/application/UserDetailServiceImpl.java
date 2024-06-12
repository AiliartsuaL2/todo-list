package moais.todolist.global.auth.application;

import lombok.RequiredArgsConstructor;
import moais.todolist.global.auth.application.usecase.CreateUserAccountUseCase;
import moais.todolist.global.auth.application.usecase.DeleteUserAccountUseCase;
import moais.todolist.global.auth.domain.UserAccount;
import moais.todolist.global.auth.persistence.UserAccountRepository;
import moais.todolist.global.exception.ErrorMessage;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class UserDetailServiceImpl implements UserDetailsService, CreateUserAccountUseCase, DeleteUserAccountUseCase {

    private final UserAccountRepository userAccountRepository;

    @Override
    public UserDetails loadUserByUsername(String memberId) throws UsernameNotFoundException {
        if (ObjectUtils.isEmpty(memberId)) {
            throw new UsernameNotFoundException(ErrorMessage.NOT_EXIST_MEMBER_ID.getMessage());
        }
        return userAccountRepository.findUserAccountByMemberId(memberId)
                .orElseThrow(() -> new UsernameNotFoundException(ErrorMessage.NOT_EXIST_USER_ACCOUNT.getMessage()));
    }

    @Override
    @Transactional
    public void create(UserAccount userAccount) {
        validateForCreate(userAccount);
        userAccountRepository.save(userAccount);
    }

    @Override
    @Transactional
    public void deleteByMemberId(String memberId) {
        loadUserByUsername(memberId);
        userAccountRepository.deleteUserAccountByMemberId(memberId);
    }

    private void validateForCreate(UserAccount userAccount) {
        if (userAccount == null) {
            throw new IllegalArgumentException(ErrorMessage.NOT_EXIST_USER_ACCOUNT.getMessage());
        }
        if (userAccountRepository.findUserAccountByMemberId(userAccount.getMemberId()).isPresent()) {
            throw new IllegalStateException(ErrorMessage.ALREADY_EXIST_USER_ACCOUNT.getMessage());
        }
    }
}
