package moais.todolist.member.application.dto.request;

import moais.todolist.global.dto.RequestDto;
import moais.todolist.member.domain.Member;
import moais.todolist.member.exception.ErrorMessage;

public record SignUpRequestDto(
        String nickname,
        String loginId,
        String password
) implements RequestDto {

    public SignUpRequestDto {
        requiredArgumentValidation(nickname, ErrorMessage.NOT_EXIST_NICKNAME.getMessage());
        requiredArgumentValidation(loginId, ErrorMessage.NOT_EXIST_LOGIN_ID.getMessage());
        requiredArgumentValidation(password, ErrorMessage.NOT_EXIST_PASSWORD.getMessage());
    }

    public Member toEntity() {
        return new Member(nickname, loginId, password);
    }
}
