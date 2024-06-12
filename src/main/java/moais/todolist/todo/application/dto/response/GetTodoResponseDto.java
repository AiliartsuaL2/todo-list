package moais.todolist.todo.application.dto.response;

import moais.todolist.todo.domain.Todo;
import moais.todolist.todo.utils.DateTimeFormatUtil;

public record GetTodoResponseDto(
        String todoId,
        String content,
        String status,
        String createdAt,
        String updatedAt
) {
    public static GetTodoResponseDto of(Todo todo) {
        return new GetTodoResponseDto(
                todo.getId(),
                todo.getContent(),
                todo.getStatus().getName(),
                DateTimeFormatUtil.toString(todo.getCreatedAt()),
                DateTimeFormatUtil.toString(todo.getModifiedAt())
        );
    }
}
