package moais.todolist.member.application.dto.request;

import moais.todolist.global.dto.RequestDto;
import moais.todolist.member.exception.ErrorMessage;

public record SignInRequestDto(
        String loginId,
        String password
) implements RequestDto {

    public SignInRequestDto {
        requiredArgumentValidation(loginId, ErrorMessage.NOT_EXIST_LOGIN_ID.getMessage());
        requiredArgumentValidation(password, ErrorMessage.NOT_EXIST_PASSWORD.getMessage());
    }
}
