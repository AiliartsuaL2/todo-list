package moais.todolist.todo.application.usecase;

import moais.todolist.todo.application.dto.response.GetTodoResponseDto;

public interface GetRecentTodoUseCase {

    GetTodoResponseDto getRecentTodo(String memberId);
}
