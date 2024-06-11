package moais.todolist.member.application.usecase;

import moais.todolist.member.application.dto.request.WithdrawRequestDto;

public interface WithdrawUseCase {

    void withdraw(WithdrawRequestDto requestDto, String payload);
}
