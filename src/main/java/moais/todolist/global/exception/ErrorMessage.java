package moais.todolist.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorMessage {

    NOT_EXIST_MEMBER_ID("회원 ID가 존재하지 않습니다."),

    NOT_EXIST_PAYLOAD("payload가 존재하지 않습니다."),

    NOT_EXIST_TOKEN("토큰이 존재하지 않습니다."),

    INVALID_TOKEN("유효하지 않은 토큰입니다."),

    NOT_EXIST_PROVIDER_TYPE("유효하지 않은 소셜 타입입니다."),

    UNAUTHORIZED("해당 API에 대해 인증이 되지 않았습니다."),

    PERMISSION_DENIED("해당 API에 대한 권한이 없습니다."),

    SIGNATURE_TOKEN_EXCEPTION("서버의 토큰 형식과 일치하지 않습니다."),

    MALFORMED_TOKEN("지원하지 않는 토큰 형식입니다."),

    EXPIRED_TOKEN("만료된 토큰 정보입니다."),

    INCORRECT_TOKEN_TYPE("발급되지 않은 토큰 정보입니다."),

    INVALID_MEMBER_ID("요청 메세지가 올바른 회원 ID 형식이 아닙니다."),

    NOT_EXIST_MEMBER("존재하지 않는 회원입니다."),

    NOT_EXIST_ROLE_TYPE("존재하지 않는 RoleType 입니다."),

    NOT_EXIST_USER_ACCOUNT("사용자 정보가 존재하지 않습니다."),

    ALREADY_EXIST_USER_ACCOUNT("이미 존재하는 계정 정보 입니다.");

    private final String message;
}
