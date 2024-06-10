package moais.todolist.global.auth.application.usecase;

import moais.todolist.global.auth.domain.Token;

public interface CreateTokenUseCase {
    Token create(String memberId);
}
