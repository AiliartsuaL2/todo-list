package moais.todolist.todo.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import moais.todolist.todo.exception.ErrorMessage;

@AllArgsConstructor
@Getter
public enum TodoStatus {
    TODO("todo", "TODO", "할 일"),
    IN_PROGRESS("inProgress", "IN PROGRESS", "진행중"),
    DONE("done", "DONE", "완료"),
    PENDING("pending", "PENDING", "대기");

    private final String value;
    private final String persistValue;
    private final String name;


    public static TodoStatus findByValue(String value) {
        for (TodoStatus todoStatus : values()) {
            if (todoStatus.value.equals(value)) {
                return todoStatus;
            }
        }
        throw new IllegalArgumentException(ErrorMessage.NOT_EXIST_TODO_STATUS.getMessage());
    }
}
