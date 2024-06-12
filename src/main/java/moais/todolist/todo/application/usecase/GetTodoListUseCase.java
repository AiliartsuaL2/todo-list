package moais.todolist.todo.application.usecase;

import moais.todolist.todo.application.dto.response.GetTodoListResponseDto;

public interface GetTodoListUseCase {

    GetTodoListResponseDto getTodoList(String memberId, int page);
}
