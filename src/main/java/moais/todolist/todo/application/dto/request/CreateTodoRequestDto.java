package moais.todolist.todo.application.dto.request;

import moais.todolist.global.dto.RequestDto;
import moais.todolist.todo.exception.ErrorMessage;

public record CreateTodoRequestDto(
        String content
) implements RequestDto {
    public CreateTodoRequestDto {
        requiredArgumentValidation(content, ErrorMessage.NOT_EXIST_CONTENT.getMessage());
    }
}
