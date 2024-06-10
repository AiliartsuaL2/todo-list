package moais.todolist.member.application.dto.response;

import moais.todolist.global.auth.domain.Token;

public record SignInResponseDto (
        String memberId,
        Token token
) {
}
