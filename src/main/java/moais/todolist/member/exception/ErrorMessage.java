package moais.todolist.member.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorMessage {

    NOT_EXIST_NICKNAME("닉네임이 존재하지 않습니다."),
    NOT_EXIST_MEMBER_ID("회원 ID가 존재하지 않습니다."),
    NOT_EXIST_LOGIN_ID("로그인 ID가 존재하지 않습니다."),
    NOT_EXIST_PASSWORD("비밀번호가 존재하지 않습니다."),
    NOT_MATCHED_ID("ID가 일치하지 않습니다."),
    NOT_MATCHED_LOGIN_ID("로그인 ID가 일치하지 않습니다."),
    NOT_MATCHED_PASSWORD("비밀번호가 일치하지 않습니다."),
    ALREADY_EXIST_LOGIN_ID("이미 존재하는 로그인 ID 입니다."),
    NOT_EXIST_MEMBER("존재하지 않는 회원 입니다."),
    NOT_EXIST_PAYLOAD("토큰의 Payload가 존재하지 않습니다.");

    private final String message;
}
