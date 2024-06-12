package moais.todolist.todo.application.dto.response;

import java.util.List;

public record GetTodoListResponseDto(
        Long count,
        List<GetTodoResponseDto> todos
) {
}
