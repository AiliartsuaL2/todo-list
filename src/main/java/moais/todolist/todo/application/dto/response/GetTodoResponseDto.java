package moais.todolist.todo.application.dto.response;

import moais.todolist.todo.domain.Todo;

// todo 테스트 코드 작성
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
                todo.getStatus().name(),
                // todo 형식 변경
                todo.getCreatedAt().toString(),
                todo.getModifiedAt().toString()
        );
    }
}
