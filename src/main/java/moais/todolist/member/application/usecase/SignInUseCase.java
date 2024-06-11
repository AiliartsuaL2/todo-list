package moais.todolist.member.application.usecase;

import moais.todolist.member.application.dto.request.SignInRequestDto;
import moais.todolist.member.application.dto.response.SignInResponseDto;

public interface SignInUseCase {

    SignInResponseDto signIn(SignInRequestDto requestDto);
}
