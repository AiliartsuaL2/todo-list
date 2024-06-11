package moais.todolist.member.application.usecase;

import moais.todolist.member.application.dto.request.SignUpRequestDto;

public interface SignUpUseCase {

    void signUp(SignUpRequestDto requestDto);
}
