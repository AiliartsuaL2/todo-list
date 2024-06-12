package moais.todolist.todo.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorMessage {

    NOT_EXIST_CONTENT("내용이 존재하지 않습니다."),

    NOT_EXIST_MEMBER_ID("회원 ID가 존재하지 않습니다."),

    OVER_THAN_MAX_SIZE_AT_CONTENT("내용은 최대 255자까지 작성이 가능합니다."),

    NOT_EXIST_TODO_STATUS("존재하지 않는 TODO 상태 입니다."),

    PERMISSION_DENIED_TO_UPDATE("수정 권한이 없습니다."),

    PENDING_IS_ONLY_CHANGE_AT_IN_PROGRESS("대기 상태는 진행 중 상태에서만 변경이 가능합니다."),

    NOT_EXIST_TODO("TODO가 존재하지 않습니다."),

    NOT_EXIST_TODO_BY_ID("존재하지 않는 TODO 입니다."),

    NOT_EXIST_MEMBER_BY_ID("존재하지 않는 회원입니다.");


    private final String message;
}
