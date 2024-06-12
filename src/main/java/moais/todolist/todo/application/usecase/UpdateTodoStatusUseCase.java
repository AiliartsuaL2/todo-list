package moais.todolist.todo.application.usecase;

import moais.todolist.todo.application.dto.request.UpdateTodoRequestDto;

public interface UpdateTodoStatusUseCase {

    void updateStatus(String memberId, String todoId, UpdateTodoRequestDto requestDto);
}
