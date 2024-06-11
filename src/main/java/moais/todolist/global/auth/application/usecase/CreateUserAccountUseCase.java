package moais.todolist.global.auth.application.usecase;

import moais.todolist.global.auth.domain.UserAccount;

public interface CreateUserAccountUseCase {
    void create(UserAccount userAccount);
}
