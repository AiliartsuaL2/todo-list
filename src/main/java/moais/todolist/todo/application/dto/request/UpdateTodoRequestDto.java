package moais.todolist.todo.application.dto.request;

import moais.todolist.global.dto.RequestDto;
import moais.todolist.todo.exception.ErrorMessage;

public record UpdateTodoRequestDto (
        String status
) implements RequestDto {
    public UpdateTodoRequestDto {
        requiredArgumentValidation(status, ErrorMessage.NOT_EXIST_TODO_STATUS.getMessage());
    }
}
